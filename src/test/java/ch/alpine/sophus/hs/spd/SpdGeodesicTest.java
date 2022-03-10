// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdGeodesicTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new SpdRandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      Tensor q = RandomSample.of(spd);
      Scalar t = RandomVariate.of(UniformDistribution.unit());
      Tensor m1 = SpdGeodesic.INSTANCE.split(p, q, t);
      Tensor m2 = SpdGeodesic.INSTANCE.split(q, p, RealScalar.ONE.subtract(t));
      Chop._04.requireClose(m1, m2);
    }
  }

  public void testIdentity() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface spd = new SpdRandomSample(n, TriangularDistribution.with(0, 1));
      Tensor p = RandomSample.of(spd);
      Scalar t = RandomVariate.of(UniformDistribution.unit());
      Tensor m = SpdGeodesic.INSTANCE.split(p, p, t);
      Chop._04.requireClose(m, p);
    }
  }
}
