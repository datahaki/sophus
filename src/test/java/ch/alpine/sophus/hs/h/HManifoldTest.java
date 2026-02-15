// code by jph
package ch.alpine.sophus.hs.h;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.CenterMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class HManifoldTest {
  @Test
  void test() {
    CenterMean centerMean = new CenterMean(HManifold.INSTANCE.biinvariantMean());
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 6, 3);
    centerMean.apply(sequence);
  }
}
