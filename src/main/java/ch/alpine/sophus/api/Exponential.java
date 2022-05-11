// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;

/** map between manifold M and tangent space T_p M for some point p in M
 * 
 * mapping from manifold to tangent space at predefined location x in manifold M
 * log_x : M -> TxM
 * operator returns a vector that satisfies {@link VectorQ}
 * this allows to concatenate log_x(p_i) for p_1, ..., p_n into a matrix */
public interface Exponential {
  /** @param v vector in the tangent space
   * @return point in the manifold */
  Tensor exp(Tensor v);

  /** @param q point in the manifold
   * @return vector in the tangent space */
  Tensor log(Tensor q);

  /** @param point on manifold
   * @return vector in tangent space that satisfies Exp_x(vector) == point */
  Tensor vectorLog(Tensor point);

  /** @param q point in the manifold
   * @return exp(log(q).negate()) */
  default Tensor flip(Tensor q) {
    return exp(log(q).negate());
  }

  /** @param q point in the manifold
   * @return exp(log(q)/2) */
  default Tensor midpoint(Tensor q) {
    return exp(log(q).multiply(RationalScalar.HALF));
  }
}
