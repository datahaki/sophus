// code by jph
package ch.alpine.sophus.hs.spd;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.AveragingWeights;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.GeometricMean;

class WeightedGeometricMeanTest {
  @Test
  void testSimple() {
    int n = 5;
    Distribution distribution = UniformDistribution.of(0.4, 10);
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor weights = AveragingWeights.of(sequence.length());
    Tensor m1 = WeightedGeometricMean.INSTANCE.mean(sequence, weights);
    Tensor m2 = GeometricMean.of(sequence);
    Tolerance.CHOP.requireClose(m1, m2);
  }
}
