// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnLineDistanceTest extends TestCase {
  public void testS2Match() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor r = RandomSample.of(randomSampleInterface);
      Scalar v1 = SnLineDistance.INSTANCE.tensorNorm(p, q).norm(r);
      Scalar v2 = S2LineDistance.INSTANCE.tensorNorm(p, q).norm(r);
      Chop._08.requireClose(v1, v2);
    }
  }

  public void testS2Midpoint() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    Distribution distribution = UniformDistribution.unit();
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor r = SnGeodesic.INSTANCE.split(p, q, RandomVariate.of(distribution));
      Chop._10.requireAllZero(S2LineDistance.INSTANCE.tensorNorm(p, q).norm(r));
      Chop._10.requireAllZero(SnLineDistance.INSTANCE.tensorNorm(p, q).norm(r));
    }
  }

  public void testS2Cross() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor r = Normalize.with(Norm._2).apply(Cross.of(p, q));
      Scalar v1 = SnLineDistance.INSTANCE.tensorNorm(p, q).norm(r);
      Scalar v2 = S2LineDistance.INSTANCE.tensorNorm(p, q).norm(r);
      Chop._03.requireClose(v1, v2);
    }
  }
}
