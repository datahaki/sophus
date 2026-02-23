// code by jph
package ch.alpine.sophus.hs.s;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.math.api.TensorDistance;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class SnLineDistanceTest {
  private static final Clip CLIP = Clips.positive(Pi.HALF);

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5 })
  void testSimple(int d) {
    Tensor p = UnitVector.of(d + 1, 0);
    Tensor q = UnitVector.of(d + 1, 1);
    TensorDistance tensorNorm = SnLineDistance.INSTANCE.distanceToLine(p, q);
    Tensor r = UnitVector.of(d + 1, 2);
    Scalar norm = tensorNorm.distance(r);
    Tolerance.CHOP.requireClose(norm, Pi.HALF);
  }

  @Test
  void testBounded() throws ClassNotFoundException, IOException {
    for (int dimension = 2; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = new Sphere(dimension);
      for (int count = 0; count < 10; ++count) {
        Tensor p = RandomSample.of(randomSampleInterface);
        Tensor q = RandomSample.of(randomSampleInterface);
        Tensor r = RandomSample.of(randomSampleInterface);
        TensorDistance tensorNorm = SnLineDistance.INSTANCE.distanceToLine(p, q);
        Scalar norm = Serialization.copy(tensorNorm).distance(r);
        CLIP.requireInside(norm);
      }
    }
  }

  @Test
  void testMidpoint() {
    Distribution distribution = UniformDistribution.unit();
    for (int dimension = 2; dimension < 6; ++dimension) {
      RandomSampleInterface randomSampleInterface = new Sphere(dimension);
      for (int count = 0; count < 10; ++count) {
        Tensor p = RandomSample.of(randomSampleInterface);
        Tensor q = RandomSample.of(randomSampleInterface);
        Tensor r = SnManifold.INSTANCE.split(p, q, RandomVariate.of(distribution));
        Chop._10.requireAllZero(SnLineDistance.INSTANCE.distanceToLine(p, q).distance(r));
      }
    }
  }

  @Test
  void testPointFail() {
    assertThrows(Exception.class, () -> SnLineDistance.INSTANCE.distanceToLine(UnitVector.of(3, 1), UnitVector.of(3, 1)));
  }
}
