// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.Vectorize;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.lie.Symmetrize;

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

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return Vectorize.of(log(q), 0);
  }
}
