// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.ex.MatrixSqrt;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.exp.Log;

class Spd0RandomSampleTest {
  private static void _check(Tensor g, MatrixSqrt matrixSqrt) {
    Chop._06.requireClose(Inverse.of(matrixSqrt.sqrt()), matrixSqrt.sqrt_inverse());
    Chop._06.requireClose(matrixSqrt.sqrt().dot(matrixSqrt.sqrt()), g);
    Chop._06.requireClose(matrixSqrt.sqrt_inverse().dot(matrixSqrt.sqrt_inverse()), Inverse.of(g));
  }

  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 6; ++n)
      for (int count = 1; count < 10; ++count) {
        RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
        Tensor g = RandomSample.of(rsi);
        _check(g, Serialization.copy(MatrixSqrt.ofSymmetric(g)));
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(g));
      }
  }

  @Test
  void testSim() {
    for (int n = 1; n < 6; ++n)
      for (int count = 1; count < 10; ++count) {
        RandomSampleInterface rsi = new TSpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
        Tensor matrix = RandomSample.of(rsi);
        _check(matrix, MatrixSqrt.ofSymmetric(matrix));
      }
  }

  @Test
  void testNegativeDiagonal() {
    Tensor matrix = DiagonalMatrix.of(-1, -2, -3);
    _check(matrix, MatrixSqrt.ofSymmetric(matrix));
  }

  // private static final Biinvariants[] BIINVARIANTS = new Biinvariants[] { //
  // Biinvariants.LEVERAGES, Biinvariants.GARDEN, Biinvariants.HARBOR, Biinvariants.CUPOLA };
  @RepeatedTest(10)
  void testSimple(RepetitionInfo repetitionInfo) {
    int n = repetitionInfo.getCurrentRepetition();
    RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
    SpdManifold.INSTANCE.isPointQ().requireMember(RandomSample.of(rsi));
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 3, 4, 6, 10, 20 })
  void testProj(int d) {
    RandomSampleInterface rsi = new Spd0RandomSample(d, TriangularDistribution.with(0, 0.5));
    Tensor matrix = RandomSample.of(rsi);
    Tensor projct = SpdManifold.project(matrix);
    Tolerance.CHOP.requireClose(matrix, projct);
  }

  @Test
  void testTrace() {
    RandomSampleInterface rsi = new Spd0RandomSample(3, TriangularDistribution.with(0, 1));
    Tensor q = RandomSample.of(rsi);
    Tensor diag = Tensor.of(Eigensystem.ofSymmetric(q).values().stream() //
        .map(Scalar.class::cast)) //
        .maps(Log.FUNCTION);
    Tensor log = MatrixLog.of(q);
    Chop._07.requireClose(Total.ofVector(diag), Trace.of(log));
    Tensor log2 = log.dot(log);
    Chop._07.requireClose(Vector2NormSquared.of(diag), Trace.of(log2));
  }

  @Test
  void testNonSymmetricFail() {
    assertThrows(Exception.class, () -> MatrixSqrt.ofSymmetric(RandomVariate.of(UniformDistribution.of(-2, 2), 4, 4)));
  }
}
