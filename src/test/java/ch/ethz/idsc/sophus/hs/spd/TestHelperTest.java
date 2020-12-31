// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.IOException;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.MatrixSqrt;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.mat.PositiveDefiniteMatrixQ;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class TestHelperTest extends TestCase {
  private static void _check(Tensor g, MatrixSqrt matrixSqrt) {
    Chop._06.requireClose(Inverse.of(matrixSqrt.sqrt()), matrixSqrt.sqrt_inverse());
    Chop._06.requireClose(matrixSqrt.sqrt().dot(matrixSqrt.sqrt()), g);
    Chop._06.requireClose(matrixSqrt.sqrt_inverse().dot(matrixSqrt.sqrt_inverse()), Inverse.of(g));
  }

  public void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 6; ++n)
      for (int count = 1; count < 10; ++count) {
        Tensor g = TestHelper.generateSpd(n);
        _check(g, Serialization.copy(MatrixSqrt.ofSymmetric(g)));
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(g));
      }
  }

  public void testSim() {
    for (int n = 1; n < 6; ++n)
      for (int count = 1; count < 10; ++count) {
        Tensor matrix = TestHelper.generateSim(n);
        _check(matrix, MatrixSqrt.ofSymmetric(matrix));
      }
  }

  public void testNegativeDiagonal() {
    Tensor matrix = DiagonalMatrix.of(-1, -2, -3);
    _check(matrix, MatrixSqrt.ofSymmetric(matrix));
  }

  public void testNonSymmetricFail() {
    AssertFail.of(() -> MatrixSqrt.ofSymmetric(RandomVariate.of(UniformDistribution.of(-2, 2), 4, 4)));
  }
}
