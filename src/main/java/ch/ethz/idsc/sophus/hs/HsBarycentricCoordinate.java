// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.FlattenLog;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

public abstract class HsBarycentricCoordinate implements BarycentricCoordinate, Serializable {
  private final BarycentricCoordinate barycentricCoordinate;

  public HsBarycentricCoordinate(BarycentricCoordinate barycentricCoordinate) {
    this.barycentricCoordinate = Objects.requireNonNull(barycentricCoordinate);
  }

  @Override // from BarycentricCoordinate
  public final Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream().map(logAt(point)::flattenLog));
    Tensor nullsp = LeftNullSpace.of(levers);
    Tensor target = barycentricCoordinate.weights(sequence, point);
    return NormalizeAffine.of(target, PseudoInverse.of(nullsp), nullsp);
  }

  /** @param point
   * @return operator that maps points on the manifold to a vector in the tangent space at given point */
  public abstract FlattenLog logAt(Tensor point);
}
