// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.sophus.math.api.LineDistance;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;

public record HsLineDistance(HomogeneousSpace homogeneousSpace) implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);

  @Override // from LineDistance
  public HsLineDistanceLocal distanceToLine(Tensor p, Tensor q) {
    Exponential exponential = homogeneousSpace.exponential(p);
    return new HsLineDistanceLocal( //
        exponential, //
        NORMALIZE_UNLESS_ZERO.apply(exponential.log(q)));
  }
}
