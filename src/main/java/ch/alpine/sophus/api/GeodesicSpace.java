// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.itp.BinaryAverage;

/** parameterized curve/geodesic in a space of tensors */
@FunctionalInterface
public interface GeodesicSpace extends BinaryAverage {
  /** @param p
   * @param q
   * @return parametric curve that for input 0 gives p and for input 1 gives q */
  ScalarTensorFunction curve(Tensor p, Tensor q);

  @Override // from BinaryAverage
  default Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }

  /** @param p
   * @param q
   * @return midpoint along curve from p to q */
  default Tensor midpoint(Tensor p, Tensor q) {
    return split(p, q, RationalScalar.HALF);
  }
}
