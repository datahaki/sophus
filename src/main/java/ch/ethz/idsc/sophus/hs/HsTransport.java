// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** @see BasisTransform */
@FunctionalInterface
public interface HsTransport {
  /** @param orig
   * @param dest
   * @return */
  // TODO use BasisTransform for more general input
  TensorUnaryOperator shift(Tensor orig, Tensor dest);
}
