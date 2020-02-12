// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.math.MatrixSqrt;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.lie.Symmetrize;

/** SPD == Symmetric positive definite == Sym+
 * 
 * <pre>
 * Exp: sim (n) -> Sym+(n)
 * Log: Sym+(n) -> sim (n)
 * </pre>
 * 
 * Reference:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Pennec, Sommer, Fletcher, p. 79
 * 
 * @see MatrixExp
 * @see MatrixLog */
public class SpdExp implements LieExponential {
  private final Tensor pp;
  private final Tensor pn;

  public SpdExp(Tensor p) {
    MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(p);
    pp = matrixSqrt.forward();
    pn = matrixSqrt.inverse();
  }

  @Override // from LieExponential
  public Tensor exp(Tensor w) {
    return pp.dot(SpdExponential.INSTANCE.exp(Symmetrize.of(pn.dot(w).dot(pn)))).dot(pp);
  }

  @Override // from LieExponential
  public Tensor log(Tensor q) {
    return pp.dot(SpdExponential.INSTANCE.log(Symmetrize.of(pn.dot(q).dot(pn)))).dot(pp);
  }
}
