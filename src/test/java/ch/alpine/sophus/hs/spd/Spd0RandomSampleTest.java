// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.mat.ex.MatrixSqrt;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

public class Spd0RandomSampleTest {
  private static void _check(Tensor g, MatrixSqrt matrixSqrt) {
    Chop._06.requireClose(Inverse.of(matrixSqrt.sqrt()), matrixSqrt.sqrt_inverse());
    Chop._06.requireClose(matrixSqrt.sqrt().dot(matrixSqrt.sqrt()), g);
    Chop._06.requireClose(matrixSqrt.sqrt_inverse().dot(matrixSqrt.sqrt_inverse()), Inverse.of(g));
  }

  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 6; ++n)
      for (int count = 1; count < 10; ++count) {
        RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
        Tensor g = RandomSample.of(rsi);
        _check(g, Serialization.copy(MatrixSqrt.ofSymmetric(g)));
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(g));
      }
  }

  @Test
  public void testSim() {
    for (int n = 1; n < 6; ++n)
      for (int count = 1; count < 10; ++count) {
        RandomSampleInterface rsi = new TSpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
        Tensor matrix = RandomSample.of(rsi);
        _check(matrix, MatrixSqrt.ofSymmetric(matrix));
      }
  }

  @Test
  public void testNegativeDiagonal() {
    Tensor matrix = DiagonalMatrix.of(-1, -2, -3);
    _check(matrix, MatrixSqrt.ofSymmetric(matrix));
  }

  @Test
  public void testNonSymmetricFail() {
    AssertFail.of(() -> MatrixSqrt.ofSymmetric(RandomVariate.of(UniformDistribution.of(-2, 2), 4, 4)));
  }
}
