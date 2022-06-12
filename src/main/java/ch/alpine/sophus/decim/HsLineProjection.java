// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Times;

public record HsLineProjection(HomogeneousSpace homogeneousSpace) implements Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);

  public Tensor onto(Tensor p, Tensor q, Tensor r) {
    // TODO SOPHUS ALG magic const
    for (int count = 0; count < 6; ++count) {
      Exponential exponential = homogeneousSpace.exponential(p);
      Tensor lq = exponential.log(q);
      // TODO SOPHUS ALG not generic: log not always vector, metric different
      Tensor normal = NORMALIZE_UNLESS_ZERO.apply(lq);
      Tensor lr = exponential.log(r);
      Tensor project = Times.of(lr.dot(normal), normal);
      p = exponential.exp(project);
    }
    return p;
  }
}
