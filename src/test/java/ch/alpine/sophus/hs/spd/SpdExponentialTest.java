// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

public class SpdExponentialTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      Tensor q = RandomSample.of(spd);
      SpdExponential spdExp = Serialization.copy(new SpdExponential(p));
      Tensor w = spdExp.log(q);
      Tensor exp = spdExp.exp(w);
      Chop._08.requireClose(q, exp);
      Tensor f1 = spdExp.flip(q);
      Tensor f2 = spdExp.exp(w.negate());
      Chop._08.requireClose(f1, f2);
      Tensor m1 = spdExp.midpoint(q);
      Tensor m2 = spdExp.exp(w.multiply(RationalScalar.HALF));
      Chop._08.requireClose(m1, m2);
    }
  }

  @Test
  public void testSpdToSym() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      SymmetricMatrixQ.require(MatrixLog.of(p), Chop._07);
    }
  }

  @Test
  public void testMidpoint() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      Tensor q = RandomSample.of(spd);
      SpdExponential spdExpP = new SpdExponential(p);
      SpdExponential spdExpQ = new SpdExponential(q);
      Tensor pqw = spdExpP.log(q);
      Tensor qpw = spdExpQ.log(p);
      Tensor ph = spdExpP.exp(pqw.multiply(RationalScalar.HALF));
      Tensor qh = spdExpQ.exp(qpw.multiply(RationalScalar.HALF));
      Chop._08.requireClose(ph, qh);
      Tensor vector = spdExpP.vectorLog(q);
      VectorQ.requireLength(vector, n * (n + 1) / 2);
    }
  }

  @Test
  public void testIdentity() {
    for (int n = 1; n < 4; ++n) {
      Exponential exponential = new SpdExponential(IdentityMatrix.of(n));
      RandomSampleInterface rsi = new TSpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor x = RandomSample.of(rsi);
      Chop._08.requireClose(exponential.exp(x), Spd0Exponential.INSTANCE.exp(x));
      RandomSampleInterface spd = new Spd0RandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor q = RandomSample.of(spd);
      Chop._08.requireClose(exponential.log(q), Spd0Exponential.INSTANCE.log(q));
      Chop._08.requireClose(exponential.vectorLog(q), Spd0Exponential.INSTANCE.vectorLog(q));
    }
  }

  @Test
  public void testNonSymmetricFail() {
    assertThrows(Exception.class, () -> new SpdExponential(RandomVariate.of(UniformDistribution.of(-2, 2), 3, 3)));
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> new SpdExponential(null));
  }
}
