// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdPointExponentialTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface rsi = new SpdRandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(rsi);
      Tensor q = RandomSample.of(rsi);
      Tensor w = SpdPointExponential.log(p, q);
      Tensor exp = SpdPointExponential.exp(p, w);
      Chop._08.requireClose(q, exp);
    }
  }

  public void testMidpoint() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface rsi = new SpdRandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(rsi);
      Tensor q = RandomSample.of(rsi);
      Tensor pqw = SpdPointExponential.log(p, q);
      Tensor qpw = SpdPointExponential.log(q, p);
      Tensor ph = SpdPointExponential.exp(p, pqw.multiply(RationalScalar.HALF));
      Tensor qh = SpdPointExponential.exp(q, qpw.multiply(RationalScalar.HALF));
      Chop._08.requireClose(ph, qh);
    }
  }
}
