// code by jph
package ch.alpine.sophus.hs.s2;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.hs.sn.SnLineDistance;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class S2LineDistanceTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    TensorNorm tensorNorm = Serialization.copy(S2LineDistance.INSTANCE.tensorNorm(Tensors.vector(1, 0, 0), Tensors.vector(0, 1, 0)));
    Chop._12.requireZero(tensorNorm.norm(Tensors.vector(-1, 0, 0)));
    Chop._12.requireClose(tensorNorm.norm(Tensors.vector(0, 0, +1)), Pi.HALF);
    Chop._12.requireClose(tensorNorm.norm(Tensors.vector(0, 0, -1)), Pi.HALF);
  }

  @Test
  void testS2Match() {
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

  @Test
  void testS2Midpoint() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    Distribution distribution = UniformDistribution.unit();
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor r = SnManifold.INSTANCE.split(p, q, RandomVariate.of(distribution));
      Chop._10.requireAllZero(S2LineDistance.INSTANCE.tensorNorm(p, q).norm(r));
      Chop._10.requireAllZero(SnLineDistance.INSTANCE.tensorNorm(p, q).norm(r));
    }
  }

  @Test
  void testS2Cross() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor r = Vector2Norm.NORMALIZE.apply(Cross.of(p, q));
      Scalar v1 = SnLineDistance.INSTANCE.tensorNorm(p, q).norm(r);
      Scalar v2 = S2LineDistance.INSTANCE.tensorNorm(p, q).norm(r);
      Chop._03.requireClose(v1, v2);
    }
  }
}
