// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.itp.BinaryAverage;

/** parameterized curve/geodesic in a space of tensors */
public interface GeodesicSpace extends MidpointInterface, BinaryAverage {
  /** @param p
   * @param q
   * @return parametric curve that for input 0 gives p and for input 1 gives q */
  ScalarTensorFunction curve(Tensor p, Tensor q);

  @Override // from MidpointInterface
  default Tensor midpoint(Tensor p, Tensor q) {
    return split(p, q, RationalScalar.HALF);
  }
}