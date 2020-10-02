// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class MatrixSqrtTest extends TestCase {
  private static void _check(Tensor g, MatrixSqrt matrixSqrt) {
    Chop._08.requireClose(Inverse.of(matrixSqrt.forward()), matrixSqrt.inverse());
    Chop._08.requireClose(matrixSqrt.forward().dot(matrixSqrt.forward()), g);
    Chop._08.requireClose(matrixSqrt.inverse().dot(matrixSqrt.inverse()), Inverse.of(g));
  }

  public void testIdentityMatrix() {
    for (int n = 1; n < 6; ++n) {
      Tensor matrix = IdentityMatrix.of(n);
      MatrixSqrt matrixSqrt = MatrixSqrt.ofSymmetric(matrix);
      _check(matrix, matrixSqrt);
      Chop._12.requireClose(matrixSqrt.forward(), IdentityMatrix.of(n));
      Chop._12.requireClose(matrixSqrt.inverse(), IdentityMatrix.of(n));
    }
  }

  public void testNegativeDiagonal() {
    Tensor matrix = DiagonalMatrix.of(-1, -2, -3);
    _check(matrix, MatrixSqrt.ofSymmetric(matrix));
  }

  public void testNonSymmetricFail() {
    AssertFail.of(() -> MatrixSqrt.ofSymmetric(RandomVariate.of(UniformDistribution.of(-2, 2), 4, 4)));
  }

  public void testNullFail() {
    AssertFail.of(() -> MatrixSqrt.ofSymmetric(null));
  }
}
