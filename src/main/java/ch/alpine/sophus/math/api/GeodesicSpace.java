// code by jph
package ch.alpine.sophus.math.api;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.itp.BinaryAverage;

/** parameterized curve/geodesic in a space of tensors */
public interface GeodesicSpace extends BinaryAverage {
  /** @param p
   * @param q
   * @return parametric curve that for input 0 gives p and for input 1 gives q */
  ScalarTensorFunction curve(Tensor p, Tensor q);

  @Override // from BinaryAverage
  default Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
