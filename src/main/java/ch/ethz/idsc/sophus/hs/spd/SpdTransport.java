// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixSqrt;

/** References:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.27 
 * 
 * @see PoleLadder */
public enum SpdTransport implements HsTransport {
  INSTANCE;

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    return new Inner(p, q);
  }

  private static class Inner implements TensorUnaryOperator {
    private final Tensor pt;

    public Inner(Tensor p, Tensor q) {
      Tensor w = new SpdExponential(p).log(q);
      MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(p);
      Tensor ps = matrixSqrt.sqrt();
      Tensor pi = matrixSqrt.sqrt_inverse();
      Tensor mid = MatrixExp.of(pi.dot(w).dot(pi).multiply(RationalScalar.HALF));
      pt = Transpose.of(ps.dot(mid).dot(pi));
    }

    /** @param v is a symmetric matrix but is treated as rank 1
     * @return symmetric matrix */
    @Override
    public Tensor apply(Tensor v) {
      return BasisTransform.ofForm(v, pt);
    }
  }
}
