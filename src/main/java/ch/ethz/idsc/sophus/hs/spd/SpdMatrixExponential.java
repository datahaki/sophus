// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.Eigensystem;
import ch.ethz.idsc.tensor.sca.AbsSquared;
import ch.ethz.idsc.tensor.sca.Log;

/** SPD == Symmetric positive definite == Sym+
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
public enum SpdMatrixExponential implements Exponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor x) {
    return Symmetrize.of(MatrixExp.ofSymmetric(x));
  }

  @Override // from Exponential
  public Tensor log(Tensor g) {
    return Symmetrize.of(MatrixLog.ofSymmetric(g));
  }

  /** n(g) == n(Inverse[g])
   * 
   * @param g spd
   * @return */
  public static Scalar nSquared(Tensor g) {
    Eigensystem eigensystem = Eigensystem.ofSymmetric(g);
    return eigensystem.values().stream() //
        .map(Scalar.class::cast) //
        .map(Log.FUNCTION) //
        .map(AbsSquared.FUNCTION) //
        .reduce(Scalar::add) //
        .get();
  }
}
