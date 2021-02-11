// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Total;

/** indefinite non-degenerate symmetric bilinear form
 * with diagonal representation
 * [1, 1, ..., 1, -1]
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016 */
/* package */ enum LBilinearForm {
  ;
  /** @param p point or tangent vector
   * @param q point or tangent vector
   * @return */
  public static Scalar between(Tensor p, Tensor q) {
    Tensor pq = p.pmul(q);
    pq.set(Scalar::negate, pq.length() - 1); // toggle sign in last entry
    return Total.ofVector(pq);
  }

  public static Scalar normSquared(Tensor x) {
    return between(x, x);
  }
}
