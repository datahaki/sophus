// code by jph
package ch.alpine.sophus.lie.td;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.api.TensorMapping;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class TdAdMetricTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(TdGroup.INSTANCE);

  @Test
  @Disabled
  void testSimple() {
    RandomSampleInterface rsi = new Td1FRandomSample(ExponentialDistribution.standard(), UniformDistribution.of(-1, 1));
    Tensor m = RandomSample.of(rsi);
    TdAdMetric stAdMetric = new TdAdMetric(m);
    Tensor sequence = RandomSample.of(rsi, 5);
    // Tensor d1 = stAdMetric.all(sequence, m);
    Tensor shift = RandomSample.of(rsi);
    for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
      Tensor seqL = tensorMapping.slash(sequence);
      Tensor mL = tensorMapping.apply(m);
      stAdMetric.all(seqL, mL);
    }
  }
}
