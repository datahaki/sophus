// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;

public record HsLineDistance(HomogeneousSpace homogeneousSpace) implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);

  @Override // from LineDistance
  public HsLineDistanceLocal tensorNorm(Tensor p, Tensor q) {
    Exponential exponential = homogeneousSpace.exponential(p);
    return new HsLineDistanceLocal( //
        exponential, //
        NORMALIZE_UNLESS_ZERO.apply(exponential.vectorLog(q)));
  }
}
