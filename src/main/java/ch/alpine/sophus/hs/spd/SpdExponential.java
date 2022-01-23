// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.Serializable;

import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.math.LowerVectorize;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.alg.Transpose;
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
 * use with {@link MetricBiinvariant#VECTORIZE0}
 * 
 * @see MatrixExp
 * @see MatrixLog
 * @see Spd0Exponential */
public class SpdExponential implements Exponential, Serializable {
  private final Tensor p;
  private final Tensor pp;
  private final Tensor pn;

  /** @param p symmetric
   * @throws Exception if p is not symmetric */
  public SpdExponential(Tensor p) {
    this.p = p;
    MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(p);
    pp = matrixSqrt.sqrt();
    pn = matrixSqrt.sqrt_inverse();
  }

  @Override // from Exponential
  public Tensor exp(Tensor w) {
    return basis(Spd0Exponential.INSTANCE.exp(basis(w, pn)), pp);
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return basis(Spd0Exponential.INSTANCE.log(basis(q, pn)), pp);
  }

  @Override // from Exponential
  public Tensor flip(Tensor q) {
    // return basis(Spd0Exponential.INSTANCE.flip(q), p);
    return SpdManifold.INSTANCE.flip(p, q);
  }

  @Override // from Exponential
  public Tensor midpoint(Tensor q) {
    return basis(Spd0Exponential.INSTANCE.midpoint(basis(q, pn)), pp);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return LowerVectorize.of(log(q), 0);
  }

  /** @param q point in Spd
   * @return linear mapping that defines parallel transport of tangent vectors
   * along geodesic from base point p to q
   * @see SpdTransport */
  public Tensor endomorphism(Tensor q) {
    Tensor w = log(q).multiply(RationalScalar.HALF);
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
    return StaticHelper.norm(basis(q, pn));
  }

  /** @param matrix
   * @param v
   * @return
   * @see BasisTransform#ofForm(Tensor, Tensor) */
  private static Tensor basis(Tensor matrix, Tensor v) {
    return Symmetrize.of(v.dot(matrix).dot(v));
  }
}
