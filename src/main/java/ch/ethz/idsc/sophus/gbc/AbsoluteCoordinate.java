// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Power;
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
 * Ad[g].Log[g^-1.m] == Log[m.g^-1] */
/** invariance under left-action is guaranteed because
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
 * by Jan Hakenberg, 2020 */
public final class AbsoluteCoordinate extends HsProjection implements ProjectedCoordinate {
  /** @param vectorLogManifold
   * @return */
  public static ProjectedCoordinate linear(VectorLogManifold vectorLogManifold) {
    // return new AbsoluteCoordinate(flattenLogManifold, InverseNorm.of(RnNorm.INSTANCE));
    return nugenx(vectorLogManifold, s -> s);
  }

  /** @param vectorLogManifold
   * @return */
  public static ProjectedCoordinate smooth(VectorLogManifold vectorLogManifold) {
    // return new AbsoluteCoordinate(flattenLogManifold, InverseNorm.of(RnNormSquared.INSTANCE));
    return nugenx(vectorLogManifold, Power.function(2));
  }

  /** @param vectorLogManifold
   * @param target
   * @return */
  public static ProjectedCoordinate custom(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    return new AbsoluteCoordinate(vectorLogManifold, Objects.requireNonNull(target));
  }

  public static ProjectedCoordinate nugenx(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    TensorUnaryOperator target = levers -> NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
        .map(Norm._2::ofVector) //
        .map(variogram) //
        .map(Scalar::reciprocal)));
    return new AbsoluteCoordinate(vectorLogManifold, target);
  }

  /***************************************************/
  private final TensorUnaryOperator target;

  private AbsoluteCoordinate(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    super(vectorLogManifold);
    this.target = target;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = levers(sequence, point);
    Tensor nullsp = LeftNullSpace.usingQR(levers);
    return NormalizeAffine.fromNullspace(target.apply(levers), nullsp);
  }
}
