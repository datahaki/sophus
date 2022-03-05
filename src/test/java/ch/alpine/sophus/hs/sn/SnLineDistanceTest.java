// code by jph
package ch.alpine.sophus.hs.sn;

import java.io.IOException;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
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

  public void testPointFail() {
    AssertFail.of(() -> SnLineDistance.INSTANCE.tensorNorm(UnitVector.of(3, 1), UnitVector.of(3, 1)));
  }
}
