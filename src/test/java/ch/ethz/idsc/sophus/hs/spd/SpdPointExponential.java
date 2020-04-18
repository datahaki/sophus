// code by jph
package ch.ethz.idsc.sophus.hs.spd;

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
public enum SpdPointExponential {
  ;
  /** @param p
   * @param w
   * @return */
  public static Tensor exp(Tensor p, Tensor w) {
    MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(p);
    Tensor pp = matrixSqrt.forward();
    Tensor pn = matrixSqrt.inverse();
    return pp.dot(SpdMatrixExponential.INSTANCE.exp(Symmetrize.of(pn.dot(w).dot(pn)))).dot(pp);
  }

  /** @param p
   * @param q
   * @return */
  public static Tensor log(Tensor p, Tensor q) {
    MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(p);
    Tensor pp = matrixSqrt.forward();
    Tensor pn = matrixSqrt.inverse();
    return pp.dot(SpdMatrixExponential.INSTANCE.log(Symmetrize.of(pn.dot(q).dot(pn)))).dot(pp);
  }
}
