// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.mat.ex.MatrixSqrt;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

enum SoProjection {
  ;
  /** Reference:
   * geomstats/special_orthogonal : projection
   * 
   * @param x
   * @return */
  public static Tensor project(Tensor x) {
    Tensor xtx = Transpose.of(x).dot(x);
    MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(xtx);
    Tensor result = x.dot(matrixSqrt.sqrt_inverse());
    Scalar det = Det.of(result);
    Chop._10.requireClose(Abs.FUNCTION.apply(det), RealScalar.ONE);
    if (Sign.isNegative(det))
      result.set(Scalar::negate, Tensor.ALL, x.length() - 1);
    return result;
  }
}
