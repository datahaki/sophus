// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.Objects;

import ch.ethz.idsc.sophus.lie.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** barycentric coordinates for inverse distance weights
 * 
 * @see HsBiinvariantCoordinate */
// TODO make class final
public class HsBarycentricCoordinate extends HsProjection implements ProjectedCoordinate {
  private final TensorUnaryOperator target;

  /** @param barycentricCoordinate that maps a sequence and a point to a vector, for instance the inverse distances */
  public HsBarycentricCoordinate(FlattenLogManifold flattenLogManifold, TensorUnaryOperator target) {
    super(flattenLogManifold);
    this.target = Objects.requireNonNull(target);
  }

  @Override // from BarycentricCoordinate
  public final Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream().map(flattenLogManifold.logAt(point)::flattenLog));
    Tensor nullsp = LeftNullSpace.of(levers);
    return NormalizeAffine.of(target.apply(levers), PseudoInverse.of(nullsp), nullsp);
  }
}
