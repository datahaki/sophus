// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.MatrixSqrt;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.lie.Symmetrize;

/** if p == IdentityMatrix[n] then SpdExp(p) reduces to SpdExponential
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
 * @see MatrixExp
 * @see MatrixLog
 * @see SpdMatrixExponential */
public class SpdExponential implements Exponential, FlattenLog, Serializable {
  private final Tensor pp;
  private final Tensor pn;

  /** @param p symmetric
   * @throws Exception if p is not symmetric */
  public SpdExponential(Tensor p) {
    MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(p);
    pp = matrixSqrt.forward();
    pn = matrixSqrt.inverse();
  }

  @Override // from Exponential
  public Tensor exp(Tensor w) {
    return Symmetrize.of(pp.dot(SpdMatrixExponential.INSTANCE.exp(Symmetrize.of(pn.dot(w).dot(pn)))).dot(pp));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    Tensor pq = Symmetrize.of(pn.dot(q).dot(pn));
    return Symmetrize.of(pp.dot(SpdMatrixExponential.INSTANCE.log(pq)).dot(pp));
  }

  @Override
  public Tensor flattenLog(Tensor q) {
    return Flatten.of(log(q));
  }
}
