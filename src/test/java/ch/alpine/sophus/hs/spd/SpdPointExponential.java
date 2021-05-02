// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.MatrixExp;
import ch.alpine.tensor.lie.MatrixLog;
import ch.alpine.tensor.lie.MatrixSqrt;
import ch.alpine.tensor.lie.Symmetrize;

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
    Tensor pp = matrixSqrt.sqrt();
    Tensor pn = matrixSqrt.sqrt_inverse();
    return pp.dot(Spd0Exponential.INSTANCE.exp(Symmetrize.of(pn.dot(w).dot(pn)))).dot(pp);
  }

  /** @param p
   * @param q
   * @return */
  public static Tensor log(Tensor p, Tensor q) {
    MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(p);
    Tensor pp = matrixSqrt.sqrt();
    Tensor pn = matrixSqrt.sqrt_inverse();
    return pp.dot(Spd0Exponential.INSTANCE.log(Symmetrize.of(pn.dot(q).dot(pn)))).dot(pp);
  }
}
