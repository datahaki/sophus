// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsLevers;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.MetricDistances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Lie affine coordinates are generalized barycentric coordinates for
 * scattered sets of points on a Lie-group with the properties:
 * 
 * coordinates sum up to 1
 * linear reproduction
 * Biinvariant: invariant under left-, right- and inverse action
 * 
 * However, generally NOT fulfilled:
 * Lagrange property
 * non-negativity
 * 
 * Log[g.m.g^-1] == Ad[g].Log[m]
 * Log[g.m] == Ad[g].Log[m.g]
 * Log[g^-1.m] == Ad[g^-1].Log[m.g^-1]
 * Ad[g].Log[g^-1.m] == Log[m.g^-1]
 * 
 * invariance under left-action is guaranteed because
 * log [(g x)^-1 g p] == log [x^-1 p]
 * 
 * If the target mapping is Ad invariant then invariance under right action
 * and inversion is guaranteed.
 * 
 * If the target mapping correlates to inverse distances then the coordinates
 * satisfy the Lagrange property.
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020
 * 
 * @see MetricDistances */
public class MetricCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return custom(vectorLogManifold, new LeversWeighting(variogram));
  }

  /** Careful:
   * Distance may depend on sequence! In that case only the correct sequence
   * should be passed to the function {@link #weights(Tensor, Tensor)}!
   * 
   * @param vectorLogManifold
   * @param target operator with levers as input
   * @return */
  public static BarycentricCoordinate custom(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    return new MetricCoordinate(vectorLogManifold, Objects.requireNonNull(target));
  }

  /***************************************************/
  private final HsLevers hsLevers;
  private final TensorUnaryOperator target;

  private MetricCoordinate(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    hsLevers = new HsLevers(vectorLogManifold);
    this.target = target;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = hsLevers.levers(sequence, point);
    return StaticHelper.barycentric( //
        target.apply(levers), // levers as input to target
        levers);
  }
}
