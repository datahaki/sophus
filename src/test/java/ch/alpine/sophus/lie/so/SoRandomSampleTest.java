// code by jph
package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.re.Det;

class SoRandomSampleTest {
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7 })
  void testSimple(int n) throws ClassNotFoundException, IOException {
    RandomSampleInterface randomSampleInterface = Serialization.copy(SoRandomSample.of(n));
    Tensor tensor = RandomSample.of(randomSampleInterface);
    Tolerance.CHOP.requireClose(Det.of(tensor), RealScalar.ONE);
    OrthogonalMatrixQ.require(tensor);
    MatrixQ.requireSize(tensor, n, n);
    Tensor log = MatrixLog.of(tensor);
    AntisymmetricMatrixQ.require(log);
  }

  @Test
  void testZeroFail() {
    assertThrows(Exception.class, () -> SoRandomSample.of(0));
  }
}
