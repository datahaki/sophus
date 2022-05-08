// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** parameterized curve/geodesic in a space of tensors */
public interface GeodesicSpace extends SplitInterface {
  /** @param p
   * @param q
   * @return parametric curve that for input 0 gives p and for input 1 gives q */
  ScalarTensorFunction curve(Tensor p, Tensor q);
}
