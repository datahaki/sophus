// code by jph
package ch.alpine.sophus.lie.sc;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class ScGroupTest {
  @Test
  void testSimple1() {
    Distribution distribution = UniformDistribution.of(-500, 500);
    for (int count = 0; count < 10; ++count) {
      Tensor x = RandomVariate.of(distribution, 1);
      Tensor exp = ScGroup.INSTANCE.exponential0().exp(x);
      Tensor log = ScGroup.INSTANCE.exponential0().log(exp);
      Chop._10.requireClose(x, log);
    }
  }
}
