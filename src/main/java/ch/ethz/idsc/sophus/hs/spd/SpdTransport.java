// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** References:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.27 */
public enum SpdTransport implements HsTransport {
  INSTANCE;

  @Override
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    Tensor w = new SpdExponential(p).log(q);
    MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(p);
    Tensor ps = matrixSqrt.forward();
    Tensor pi = matrixSqrt.inverse();
    Tensor mid = MatrixExp.of(pi.dot(w).dot(pi).multiply(RationalScalar.HALF));
    Tensor pt = ps.dot(mid).dot(pi); // TODO check if there is a typo in the article here
    return v -> pt.dot(v).dot(Transpose.of(pt));
  }
}
