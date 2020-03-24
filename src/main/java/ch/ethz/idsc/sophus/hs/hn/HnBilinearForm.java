// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/* package */ enum HnBilinearForm {
  ;
  public static Scalar between(Tensor p, Tensor q) {
    return p.get(1).dot(q.get(1)).Get().subtract(p.Get(0).multiply(q.Get(0)));
  }
}
