// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.krg.MetricDistances;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** partition of unity
 * linear reproduction
 * Lagrange
 * C^infinity (except at points from input set)
 * 
 * in general, the coordinates may evaluate to be negative
 * 
 * Lie affine coordinates are generalized barycentric coordinates for
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
 * References:
 * "Inverse Distance Coordinates for Scattered Sets of Points"
 * by Jan Hakenberg, 2020
 * 
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020
 * 
 * @see MetricDistances */
public class MetricCoordinate implements TensorUnaryOperator {
  private static final long serialVersionUID = -8043520781023560311L;
  private static final TensorUnaryOperator AFFINE = matrix -> ConstantArray.of(RealScalar.ONE, matrix.length());

  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static TensorUnaryOperator of(ScalarUnaryOperator variogram) {
    return custom(new LeversWeighting(variogram));
  }

  /** Careful:
   * Distance may depend on sequence! In that case only the correct sequence
   * should be passed to the function {@link #weights(Tensor, Tensor)}!
   * 
   * @param vectorLogManifold
   * @param target operator with design matrix as input
   * @return */
  public static TensorUnaryOperator custom(TensorUnaryOperator target) {
    return new MetricCoordinate(Objects.requireNonNull(target));
  }

  public static TensorUnaryOperator affine() {
    return custom(AFFINE);
  }

  /***************************************************/
  private final TensorUnaryOperator target;

  private MetricCoordinate(TensorUnaryOperator target) {
    this.target = target;
  }

  @Override // from BarycentricCoordinate
  public Tensor apply(Tensor matrix) {
    return StaticHelper.barycentric( //
        target.apply(matrix), // design matrix as input to target
        matrix);
  }
}
