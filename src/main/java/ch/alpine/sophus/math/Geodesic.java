// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** parameterized curve/geodesic in a space of tensors */
// TODO perhaps rename to "GeodesicSpace" that "connect"s p and q?
public interface Geodesic extends SplitInterface {
  /** @param p
   * @param q
   * @return parametric curve that for input 0 gives p and for input 1 gives q */
  ScalarTensorFunction curve(Tensor p, Tensor q);
}
