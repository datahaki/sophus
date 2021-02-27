// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;

/** map between manifold M and tangent space T_p M for some point p in M */
public interface Exponential extends TangentSpace {
  /** @param v vector in the tangent space
   * @return point in the manifold */
  Tensor exp(Tensor v);

  /** @param q point in the manifold
   * @return vector in the tangent space */
  Tensor log(Tensor q);

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
