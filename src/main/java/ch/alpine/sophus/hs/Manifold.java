// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;

public interface Manifold extends MemberQ {
  /** @param p
   * @return exponential map at given point p on manifold */
  Exponential exponential(Tensor p);
}
