// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** parallel transport of vectors, i.e. elements from tangent space from point
 * orig to point dest
 * 
 * General methods are {@link PoleLadder} and {@link SchildLadder}.
 * 
 * TODO SOPHUS the implementation of parallel transport of tensors of higher rank is deferred
 * 
 * @see BasisTransform */
@FunctionalInterface
public interface HsTransport {
  /** the operator may take tangent vectors represented by matrices as input,
   * see for instance SpdTransport
   * 
   * @param orig point
   * @param dest point
   * @return map a tangent vector at orig to a tangent vector at dest */
  TensorUnaryOperator shift(Tensor orig, Tensor dest);
}
