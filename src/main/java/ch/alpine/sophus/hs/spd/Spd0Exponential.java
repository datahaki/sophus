// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.math.LowerVectorize;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.ex.MatrixSqrt;
import ch.alpine.tensor.mat.re.Inverse;

/** Exponential map at IdentityMatrix in SPD
 * 
 * SPD == Symmetric positive definite == Sym+
 * 
 * <pre>
 * Exp: sim (n) -> Sym+(n)
 * Log: Sym+(n) -> sim (n)
 * </pre>
 * 
 * <p>
 * Exp is equivalent to MatrixExp
 * Log is equivalent to MatrixLog
 * 
 * <p>
 * Reference:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 79
 * 
 * @see MatrixExp
 * @see MatrixLog
 * @see SpdExponential */
public enum Spd0Exponential implements Exponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor x) {
    return Symmetrize.of(MatrixExp.ofSymmetric(x));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return Symmetrize.of(MatrixLog.ofSymmetric(q));
  }

  @Override // from Exponential
  public Tensor flip(Tensor q) {
    return Inverse.of(q);
  }

  @Override // from Exponential
  public Tensor midpoint(Tensor q) {
    return MatrixSqrt.ofSymmetric(q).sqrt();
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return LowerVectorize.of(log(q), 0);
  }
}
