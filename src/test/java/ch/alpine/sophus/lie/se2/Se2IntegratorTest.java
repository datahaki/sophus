// code by jph
package ch.alpine.sophus.lie.se2;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class Se2IntegratorTest {
  @Test
  void test() {
    Tensor g = RandomVariate.of(UniformDistribution.unit(), 3);
    Tensor x = RandomVariate.of(UniformDistribution.unit(), 3);
    Tensor p1 = Se2Group.INSTANCE.tangentSpace(g).exp(x);
    Tensor p2 = Se2Group.INSTANCE.combine(g, Se2CoveringGroup.INSTANCE.lieExponential().exp(x));
    Tolerance.CHOP.requireClose(p1, p2);
  }
}
