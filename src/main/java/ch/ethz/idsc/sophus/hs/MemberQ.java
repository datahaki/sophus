// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;

public interface MemberQ {
  boolean isPoint(Tensor x);

  Tensor requirePoint(Tensor x);

  boolean isTangent(Tensor x, Tensor v);

  /** @param x
   * @param v
   * @return v */
  Tensor requireTangent(Tensor x, Tensor v);
}
