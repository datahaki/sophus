// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.Objects;

import ch.ethz.idsc.sophus.lie.FlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

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
 * satisfy the Lagrange property. */
public final class HsBarycentricCoordinate extends HsProjection implements ProjectedCoordinate {
  /** @param flattenLogManifold
   * @return */
  public static ProjectedCoordinate linear(FlattenLogManifold flattenLogManifold) {
    return new HsBarycentricCoordinate(flattenLogManifold, InverseNorm.of(RnNorm.INSTANCE));
  }

  /** @param flattenLogManifold
   * @return */
  public static ProjectedCoordinate smooth(FlattenLogManifold flattenLogManifold) {
    return new HsBarycentricCoordinate(flattenLogManifold, InverseNorm.of(RnNormSquared.INSTANCE));
  }

  /** @param flattenLogManifold
   * @param target
   * @return */
  public static ProjectedCoordinate custom(FlattenLogManifold flattenLogManifold, TensorUnaryOperator target) {
    return new HsBarycentricCoordinate(flattenLogManifold, target);
  }

  /***************************************************/
  private final TensorUnaryOperator target;

  private HsBarycentricCoordinate(FlattenLogManifold flattenLogManifold, TensorUnaryOperator target) {
    super(flattenLogManifold);
    this.target = Objects.requireNonNull(target);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream().map(flattenLogManifold.logAt(point)::flattenLog));
    Tensor nullsp = LeftNullSpace.of(levers);
    return NormalizeAffine.of(target.apply(levers), PseudoInverse.of(nullsp), nullsp);
  }
}
