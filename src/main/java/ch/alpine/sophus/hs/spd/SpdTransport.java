// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** Closed-form solution for the parallel transport of a tangent vector along the
 * geodesic. Consistent with {@link PoleLadder} method.
 * 
 * References:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.27
 * 
 * @see PoleLadder */
/* package */ enum SpdTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    Tensor pt = new SpdExponential(p).endomorphism(q);
    // v is a symmetric matrix but is treated as rank 1
    return v -> BasisTransform.ofForm(v, pt);
  }
}
