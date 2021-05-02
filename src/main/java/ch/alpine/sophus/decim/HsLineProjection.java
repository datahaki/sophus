// code by jph
package ch.alpine.sophus.decim;

import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;

public class HsLineProjection {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // ---
  private final HsManifold hsManifold;

  public HsLineProjection(HsManifold hsManifold) {
    this.hsManifold = hsManifold;
  }

  public Tensor onto(Tensor p, Tensor q, Tensor r) {
    // while (true) {
    // Exponential exponential = hsManifold.exponential(p);
    // Tensor lq = exponential.log(q);
    // Tensor normal = NORMALIZE_UNLESS_ZERO.apply(lq);
    // Tensor lr = exponential.log(r);
    // Tensor project = lr.dot(normal).pmul(normal);
    // if (Chop._08.allZero(project))
    // return p;
    // p = exponential.exp(project);
    // }
    for (int count = 0; count < 6; ++count) {
      Exponential exponential = hsManifold.exponential(p);
      Tensor lq = exponential.log(q);
      // FIXME not generic: log not always vector, metric different
      Tensor normal = NORMALIZE_UNLESS_ZERO.apply(lq);
      Tensor lr = exponential.log(r);
      Tensor project = lr.dot(normal).pmul(normal);
      p = exponential.exp(project);
    }
    return p;
  }
}
