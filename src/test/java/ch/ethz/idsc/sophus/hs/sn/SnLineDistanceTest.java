// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clip;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class SnLineDistanceTest extends TestCase {
  private static final Clip CLIP = Clips.positive(Pi.HALF);

  public void testBounded() throws ClassNotFoundException, IOException {
    for (int dimension = 2; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = 0; count < 10; ++count) {
        Tensor p = RandomSample.of(randomSampleInterface);
        Tensor q = RandomSample.of(randomSampleInterface);
        Tensor r = RandomSample.of(randomSampleInterface);
        TensorNorm tensorNorm = SnLineDistance.INSTANCE.tensorNorm(p, q);
        Scalar norm = Serialization.copy(tensorNorm).norm(r);
        CLIP.requireInside(norm);
      }
    }
  }

  public void testMidpoint() {
    Distribution distribution = UniformDistribution.unit();
    for (int dimension = 2; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      for (int count = 0; count < 10; ++count) {
        Tensor p = RandomSample.of(randomSampleInterface);
        Tensor q = RandomSample.of(randomSampleInterface);
        Tensor r = SnGeodesic.INSTANCE.split(p, q, RandomVariate.of(distribution));
        Chop._10.requireAllZero(SnLineDistance.INSTANCE.tensorNorm(p, q).norm(r));
      }
    }
  }
}
