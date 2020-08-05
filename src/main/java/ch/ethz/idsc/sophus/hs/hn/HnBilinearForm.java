// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Total;

/** indefinite non-degenerate symmetric bilinear form
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016 */
/* package */ enum HnBilinearForm {
  ;
  /** @param p point or tangent vector
   * @param q point or tangent vector
   * @return */
  public static Scalar between(Tensor p, Tensor q) {
    Tensor pq = p.pmul(q);
    pq.set(Scalar::negate, pq.length() - 1);
    return Total.ofVector(pq);
  }
}
