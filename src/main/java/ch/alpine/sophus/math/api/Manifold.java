// code by jph
package ch.alpine.sophus.math.api;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;

public interface Manifold {
  /** @param p
   * @return exponential map at given point p on manifold */
  Exponential exponential(Tensor p);

  MemberQ isPointQ();
}
