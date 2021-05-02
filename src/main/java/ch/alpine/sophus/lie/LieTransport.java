// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** for a Lie groups, parallel transport of tangent vectors
 * is assumed to be along exp geodesics.
 * 
 * In this setting, a tangent vector at the neutral element
 * uniquely defines a left invariant vector field everywhere.
 * 
 * We propose the convention (but not require) that tangent
 * vectors are stated in the basis of the tangent space at
 * the neutral element, that is the Lie algebra. */
public enum LieTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor orig, Tensor dest) {
    return Tensor::copy;
  }
}
