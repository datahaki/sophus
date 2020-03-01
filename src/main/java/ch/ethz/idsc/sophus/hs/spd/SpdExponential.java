// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.Eigensystem;
import ch.ethz.idsc.tensor.sca.AbsSquared;
import ch.ethz.idsc.tensor.sca.Exp;
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
 * @see SpdExp */
public enum SpdExponential implements LieExponential {
  INSTANCE;

  @Override // from LieExponential
  public Tensor exp(Tensor x) {
    Eigensystem eigensystem = Eigensystem.ofSymmetric(x);
    Tensor avec = eigensystem.vectors();
    Tensor ainv = Transpose.of(avec);
    return Symmetrize.of(ainv.dot(eigensystem.values().map(Exp.FUNCTION).pmul(avec)));
  }

  @Override // from LieExponential
  public Tensor log(Tensor g) {
    Eigensystem eigensystem = Eigensystem.ofSymmetric(g);
    Tensor avec = eigensystem.vectors();
    Tensor ainv = Transpose.of(avec);
    return Symmetrize.of(ainv.dot(eigensystem.values().map(Log.FUNCTION).pmul(avec)));
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
