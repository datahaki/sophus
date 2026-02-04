// code by jph
package ch.alpine.sophus.hs.rpn;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so2.ArcTan2D;
import ch.alpine.sophus.math.AveragingWeights;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.rot.AngleVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;

class RpnManifoldTest {
  @Test
  void testRp1Linear() {
    RandomGenerator randomGenerator = new Random(3);
    Distribution distribution = UniformDistribution.of(0, Math.PI / 4);
    for (int n = 2; n < 5; ++n)
      for (int count = 0; count < 5; ++count) {
        Tensor angles = RandomVariate.of(distribution, randomGenerator, n);
        Tensor sequence = angles.map(AngleVector::of);
        Tensor weights = AveragingWeights.of(n);
        Tensor point = RpnManifold.INSTANCE.biinvariantMean().mean(sequence, weights);
        Chop._12.requireClose(ArcTan2D.of(point), Mean.of(angles));
      }
  }

  @Test
  void testDistance() {
    Tolerance.CHOP.requireZero(RpnManifold.INSTANCE.distance(Tensors.vector(2, 0, 0), Tensors.vector(+10, 0, 0)));
    Tolerance.CHOP.requireZero(RpnManifold.INSTANCE.distance(Tensors.vector(2, 0, 0), Tensors.vector(-10, 0, 0)));
  }
}
