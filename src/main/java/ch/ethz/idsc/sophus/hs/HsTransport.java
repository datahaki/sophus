// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.hs.spd.SpdTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** parallel transport of vectors, i.e. elements from tangent space from point
 * orig to point dest
 * 
 * the implementation of parallel transport of tensors of higher rank is deferred
 * 
 * @see BasisTransform */
@FunctionalInterface
public interface HsTransport {
  /** the operator may take tangent vectors represented by matrices as input,
   * see for instance {@link SpdTransport}
   * 
   * @param orig point
   * @param dest point
   * @return map a tangent vector at orig to a tangent vector at dest */
  TensorUnaryOperator shift(Tensor orig, Tensor dest);
}
