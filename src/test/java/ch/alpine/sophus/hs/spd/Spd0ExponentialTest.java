// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.LowerTriangularize;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class Spd0ExponentialTest {
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 4 })
  void testSimple(int n) {
    RandomSampleInterface rsi = new TSpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
    Tensor x = RandomSample.of(rsi);
    Tensor g = Spd0Exponential.INSTANCE.exp(x);
    Tensor r = Spd0Exponential.INSTANCE.log(g);
    Chop._07.requireClose(x, r);
    Tensor m1 = Spd0Exponential.INSTANCE.exp(Spd0Exponential.INSTANCE.log(g).multiply(Rational.HALF));
    Tensor m2 = SpdManifold.sqrt(g);
    Chop._07.requireClose(m1, m2);
    Tensor m3 = SpdManifold.INSTANCE.midpoint(IdentityMatrix.of(n), g);
    Chop._07.requireClose(m1, m3);
  }

  @Test
  void testMatrixExp() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface rsi = new TSpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor x = RandomSample.of(rsi);
      Tensor exp1 = Spd0Exponential.INSTANCE.exp(x);
      Tensor exp2 = MatrixExp.of(x);
      Chop._07.requireClose(exp1, exp2);
    }
  }

  @Test
  void testMatrixLog() {
    Spd0RandomSample spdRandomSample = new Spd0RandomSample(2, UniformDistribution.of(Clips.absolute(1)));
    for (int count = 0; count < 10; ++count) {
      Tensor x = RandomSample.of(spdRandomSample);
      Tensor exp1 = Spd0Exponential.INSTANCE.log(x);
      Tensor exp2 = MatrixLog.of(x);
      Chop._08.requireClose(exp1, exp2);
    }
  }

  @Test
  void testSimple2() {
    assertEquals(Spd0Exponential.norm(IdentityMatrix.of(3)), RealScalar.ZERO);
  }

  @Test
  void testExpNonSymmetricFail() {
    RandomSampleInterface rsi = new TSpdRandomSample(4, UniformDistribution.of(Clips.absolute(1)));
    Tensor x = LowerTriangularize.of(RandomSample.of(rsi));
    assertThrows(Exception.class, () -> Spd0Exponential.INSTANCE.exp(x));
  }

  @Test
  void testLogNonSymmetricFail() {
    Spd0RandomSample spdRandomSample = new Spd0RandomSample(4, UniformDistribution.of(Clips.absolute(1)));
    Tensor g = LowerTriangularize.of(RandomSample.of(spdRandomSample));
    assertThrows(Exception.class, () -> Spd0Exponential.INSTANCE.log(g));
  }
}
