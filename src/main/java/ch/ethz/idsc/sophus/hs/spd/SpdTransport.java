// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixSqrt;

/** References:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.27 */
public enum SpdTransport implements HsTransport {
  INSTANCE;

  @Override
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
      pt = ps.dot(mid).dot(pi); // TODO check if there is a typo in the article here
    }

    @Override
    public Tensor apply(Tensor v) {
      return pt.dot(v).dot(Transpose.of(pt)); // TODO precompute Transpose
    }
  }
}
