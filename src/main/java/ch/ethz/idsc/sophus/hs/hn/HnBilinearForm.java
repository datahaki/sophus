// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;

/** indefinite nondegenerate symmetric bilinear form
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016 */
/* package */ enum HnBilinearForm {
  ;
  public static Scalar between(Tensor p, Tensor q) {
    int n = p.length();
    VectorQ.requireLength(q, n);
    Scalar x0 = p.Get(0).multiply(q.Get(0));
    Scalar xn = p.extract(1, n).dot(q.extract(1, n)).Get();
    return xn.subtract(x0);
  }
}
