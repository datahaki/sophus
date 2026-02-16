// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class SpdExponentialTest {
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 4 })
  void testSimple(int n) {
    Random randomGenerator = new Random(3);
    RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
    Tensor p = RandomSample.of(spd, randomGenerator);
    Tensor q = RandomSample.of(spd, randomGenerator);
    SpdExponential exp_p = new SpdExponential(p);
    Tensor w = exp_p.log(q);
    Tensor exp = exp_p.exp(w);
    Chop._08.requireClose(q, exp);
    Tensor f1 = SpdManifold.INSTANCE.flip(p, q);
    Tensor f2 = exp_p.exp(w.negate());
    Chop._08.requireClose(f1, f2);
    Tensor m1 = SpdManifold.INSTANCE.midpoint(p, q);
    Tensor m2 = exp_p.exp(w.multiply(Rational.HALF));
    Chop._08.requireClose(m1, m2);
  }

  @Test
  void testSpdToSym() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      new SymmetricMatrixQ(Chop._07).require(MatrixLog.of(p));
    }
  }

  @Test
  void testMidpoint() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      Tensor q = RandomSample.of(spd);
      SpdExponential spdExpP = new SpdExponential(p);
      SpdExponential spdExpQ = new SpdExponential(q);
      Tensor pqw = spdExpP.log(q);
      Tensor qpw = spdExpQ.log(p);
      Tensor ph = spdExpP.exp(pqw.multiply(Rational.HALF));
      Tensor qh = spdExpQ.exp(qpw.multiply(Rational.HALF));
      Chop._08.requireClose(ph, qh);
      // Tensor vector = spdExpP.vectorLog(q);
      // VectorQ.requireLength(vector, n * (n + 1) / 2);
    }
  }

  @Test
  void testIdentity() {
    for (int n = 1; n < 4; ++n) {
      Exponential exponential = new SpdExponential(IdentityMatrix.of(n));
      RandomSampleInterface rsi = new TSpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor x = RandomSample.of(rsi);
      Chop._08.requireClose(exponential.exp(x), Spd0Exponential.INSTANCE.exp(x));
      RandomSampleInterface spd = new Spd0RandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor q = RandomSample.of(spd);
      Chop._08.requireClose(exponential.log(q), Spd0Exponential.INSTANCE.log(q));
      // Chop._08.requireClose(exponential.vectorLog(q), Spd0Exponential.INSTANCE.vectorLog(q));
    }
  }

  @Test
  void testNonSymmetricFail() {
    assertThrows(Exception.class, () -> new SpdExponential(RandomVariate.of(UniformDistribution.of(-2, 2), 3, 3)));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> new SpdExponential(null));
  }
}
