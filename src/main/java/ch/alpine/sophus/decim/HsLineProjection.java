// code by jph
package ch.alpine.sophus.decim;

import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Times;

public class HsLineProjection {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // ---
  private final HsManifold hsManifold;

  public HsLineProjection(HsManifold hsManifold) {
    this.hsManifold = hsManifold;
  }

  public Tensor onto(Tensor p, Tensor q, Tensor r) {
    // TODO SOPHUS ALG magic const
    for (int count = 0; count < 6; ++count) {
      Exponential exponential = hsManifold.exponential(p);
      Tensor lq = exponential.log(q);
      // FIXME SOPHUS ALG not generic: log not always vector, metric different
      Tensor normal = NORMALIZE_UNLESS_ZERO.apply(lq);
      Tensor lr = exponential.log(r);
      Tensor project = Times.of(lr.dot(normal), normal);
      p = exponential.exp(project);
    }
    return p;
  }
}
