package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

@FunctionalInterface
public interface HsTransport {
  /** @param orig
   * @param dest
   * @return */
  TensorUnaryOperator shift(Tensor orig, Tensor dest);
}
