// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixSqrt;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.sca.Abs;

public enum SoMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor x) {
    return Tolerance.CHOP.isClose(Det.of(x), RealScalar.ONE) //
        && OrthogonalMatrixQ.of(x);
  }

  /** Reference:
   * geomstats/special_orthogonal : projection
   * 
   * @param x
   * @return */
  public static Tensor project(Tensor x) {
    // TODO SOPHUS should be special case of StProjection
    Tensor xtx = Transpose.of(x).dot(x);
    // TODO SOPHUS use ofSymmetric 
    MatrixSqrt matrixSqrt = MatrixSqrt.of(xtx);
    Tensor result = x.dot(matrixSqrt.sqrt_inverse());
    Scalar det = Det.of(result);
    Tolerance.CHOP.requireClose(Abs.FUNCTION.apply(det), RealScalar.ONE);
    if (Tolerance.CHOP.isClose(det, RealScalar.ONE.negate()))
      result.set(Scalar::negate, Tensor.ALL, x.length() - 1);
    return result;
  }
}
