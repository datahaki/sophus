// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/MemberQ.html">MemberQ</a> */
public interface MemberQ {
  /** @param x point
   * @return whether given point x is member of manifold */
  boolean isPoint(Tensor x);

  /** @param x
   * @return given point x
   * @throws Exception if point is not member of manifold */
  Tensor requirePoint(Tensor x);

  /** @param x
   * @param v
   * @return whether v is vector in tangent space at point x */
  boolean isTangent(Tensor x, Tensor v);

  /** @param x
   * @param v
   * @return v
   * @throws Exception if v is not vector in tangent space at point x */
  Tensor requireTangent(Tensor x, Tensor v);
}
