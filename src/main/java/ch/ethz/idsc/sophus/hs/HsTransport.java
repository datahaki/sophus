// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** @see BasisTransform */
@FunctionalInterface
public interface HsTransport {
  /** @param orig point
   * @param dest point
   * @return map a tangent vector at orig to a tangent vector at dest */
  // TODO use BasisTransform for more general input
  TensorUnaryOperator shift(Tensor orig, Tensor dest);
}
