// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.tensor.Tensor;

public interface CurveSubdivision {
  /** @param tensor
   * @return one round of subdivision of closed curve defined by given tensor */
  Tensor cyclic(Tensor tensor);

  /** @param tensor
   * @return one round of subdivision of non-closed curve defined by given tensor */
  Tensor string(Tensor tensor);
}
