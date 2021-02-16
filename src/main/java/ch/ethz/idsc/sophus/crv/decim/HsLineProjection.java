// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.nrm.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;

public class HsLineProjection {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(VectorNorm2::of);
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
      Tensor normal = NORMALIZE_UNLESS_ZERO.apply(lq);
      Tensor lr = exponential.log(r);
      Tensor project = lr.dot(normal).pmul(normal);
      p = exponential.exp(project);
    }
    return p;
  }
}
