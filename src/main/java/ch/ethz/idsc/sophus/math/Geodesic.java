// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;

/** parameterized curve/geodesic in a space of tensors */
public interface Geodesic extends SplitInterface {
  /** @param p
   * @param q
   * @return parametric curve that for input 0 gives p and for input 1 gives q */
  ScalarTensorFunction curve(Tensor p, Tensor q);
}
