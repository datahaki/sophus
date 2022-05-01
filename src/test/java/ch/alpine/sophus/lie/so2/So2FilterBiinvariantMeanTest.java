// code by jph
package ch.alpine.sophus.lie.so2;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class So2FilterBiinvariantMeanTest {
  @Test
  public void testLength2Permutations() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    Distribution wd = UniformDistribution.of(-3, 3);
    for (int count = 0; count < 100; ++count) {
      Tensor sequence = RandomVariate.of(distribution, 2);
      Scalar w = RandomVariate.of(wd);
      Tensor weights = Tensors.of(RealScalar.ONE.subtract(w), w);
      Scalar mean1 = So2FilterBiinvariantMean.INSTANCE.mean(sequence, weights);
      Scalar mean2 = So2FilterBiinvariantMean.INSTANCE.mean(Reverse.of(sequence), Reverse.of(weights));
      Chop._13.requireClose(mean1, mean2);
    }
  }
}
