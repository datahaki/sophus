// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.FlattenLog;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** barycentric coordinates for inverse distance weights
 * 
 * @see HsBiinvariantCoordinate */
public abstract class HsBarycentricCoordinate implements ProjectedCoordinate, Serializable {
  private final TensorUnaryOperator target;

  /** @param barycentricCoordinate that maps a sequence and a point to a vector, for instance the inverse distances */
  public HsBarycentricCoordinate(TensorUnaryOperator target) {
    this.target = Objects.requireNonNull(target);
  }

  @Override // from BarycentricCoordinate
  public final Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream().map(logAt(point)::flattenLog));
    Tensor nullsp = LeftNullSpace.of(levers);
    return NormalizeAffine.of(target.apply(levers), PseudoInverse.of(nullsp), nullsp);
  }

  @Override // from ProjectionInterface
  public final Tensor projection(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream().map(logAt(point)::flattenLog));
    Tensor nullsp = LeftNullSpace.of(levers);
    return PseudoInverse.of(nullsp).dot(nullsp);
  }

  /** @param point
   * @return operator that maps points on the manifold to a vector in the tangent space at given point */
  public abstract FlattenLog logAt(Tensor point);
}
