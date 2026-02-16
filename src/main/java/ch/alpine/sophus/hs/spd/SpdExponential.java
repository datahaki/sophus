// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.Serializable;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.math.UpperVectorize;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.ex.MatrixSqrt;

/** if p == IdentityMatrix[n] then SpdExponential(p) reduces to Spd0Exponential
 * 
 * SPD == Symmetric positive definite == Sym+
 * 
 * <pre>
 * Exp: sim (n) -> Sym+(n)
 * Log: Sym+(n) -> sim (n)
 * </pre>
 * 
 * Reference:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 79
 * 
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.27
 * 
 * @see MatrixExp
 * @see MatrixLog
 * @see Spd0Exponential */
public class SpdExponential implements Exponential, Serializable {
  private final Tensor pp;
  private final Tensor pn;

  /** @param p symmetric
   * @throws Exception if p is not symmetric */
  public SpdExponential(Tensor p) {
    MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(p);
    pp = matrixSqrt.sqrt();
    pn = matrixSqrt.sqrt_inverse();
  }

  public Tensor pp() {
    return pp;
  }

  public Tensor pn() {
    return pn;
  }

  @Override // from Exponential
  public Tensor exp(Tensor w) {
    return basis(Spd0Exponential.INSTANCE.exp(basis(w, pn)), pp);
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return basis(Spd0Exponential.INSTANCE.log(basis(q, pn)), pp);
  }

  @Override
  public TensorUnaryOperator vectorLog() {
    return q -> UpperVectorize.of(log(q), 0);
  }

  @Override
  public ZeroDefectArrayQ isTangentQ() {
    return TSpdMemberQ.INSTANCE;
  }

  /** @param q point in Spd
   * @return linear mapping that defines parallel transport of tangent vectors
   * along geodesic from base point p to q
   * @see SpdTransport */
  public Tensor endomorphism(Tensor q) {
    Tensor w = log(q).multiply(Rational.HALF);
    Tensor mid = Spd0Exponential.INSTANCE.exp(basis(w, pn));
    return Transpose.of(pp.dot(mid).dot(pn)); // mid is treated as (1, 1) tensor
  }

  /** Reference:
   * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
   * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 82
   * 
   * @param q
   * @return the 2-norm of the eigenvalues of log_p(q) == log_0(bt(q, pn))
   * @see SpdMetric */
  /* package */ Scalar distance(Tensor q) {
    return Spd0Exponential.norm(basis(q, pn));
  }

  /** @param matrix
   * @param v
   * @return
   * @see BasisTransform#ofForm(Tensor, Tensor) */
  /* package */ static Tensor basis(Tensor matrix, Tensor v) {
    return Symmetrize.of(v.dot(matrix).dot(v));
  }
}
