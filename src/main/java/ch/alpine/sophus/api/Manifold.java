// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;

public interface Manifold {
  /** @param p
   * @return exponential map at given point p on manifold */
  TangentSpace tangentSpace(Tensor p);

  MemberQ isPointQ();
}
