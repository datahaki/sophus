// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** Closed-form solution for the parallel transport of a tangent vector along the
 * geodesic. Consistent with {@link PoleLadder} method.
 * 
 * References:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.27
 * 
 * @see PoleLadder */
public enum SpdTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    Tensor pt = new SpdExponential(p).endomorphism(q);
    // v is a symmetric matrix but is treated as rank 1
    return v -> BasisTransform.ofForm(v, pt);
  }
}
