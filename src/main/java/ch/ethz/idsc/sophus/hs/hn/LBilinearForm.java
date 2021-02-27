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
 * "Metric Spaces of Non-Positive Curvature" p. 38
 * by Martin R. Bridson, Andre Haefliger, 1999
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016 */
/* package */ enum LBilinearForm {
  ;
  /** for all p, q in H^n one has <u|v> <= -1, with equality if and only if p = q.
   * 
   * @param p point or tangent vector
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
