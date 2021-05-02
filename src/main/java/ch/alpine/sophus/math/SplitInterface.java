// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.itp.BinaryAverage;

@FunctionalInterface
public interface SplitInterface extends MidpointInterface, BinaryAverage {
  @Override // from MidpointInterface
  default Tensor midpoint(Tensor p, Tensor q) {
    return split(p, q, RationalScalar.HALF);
  }
}
