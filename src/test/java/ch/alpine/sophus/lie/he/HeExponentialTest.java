// code by jph
package ch.alpine.sophus.lie.he;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

class HeExponentialTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(HeGroup.INSTANCE);
  private static final RandomSampleInterface RSI = new HeRandomSample(2, UniformDistribution.of(Clips.absolute(10)));

  @Test
  public void testExpLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = RandomSample.of(RSI);
      Tensor xyz = HeExponential.INSTANCE.exp(inp);
      Tensor uvw = HeExponential.INSTANCE.log(xyz);
      Tolerance.CHOP.requireClose(inp, uvw);
    }
  }

  @Test
  public void testLogExp() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = RandomSample.of(RSI);
      Tensor uvw = HeExponential.INSTANCE.log(inp);
      Tensor xyz = HeExponential.INSTANCE.exp(uvw);
      Tolerance.CHOP.requireClose(inp, xyz);
    }
  }

  @Test
  public void testAdLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomSample.of(RSI);
      Tensor m = RandomSample.of(RSI);
      Tensor lhs = HeExponential.INSTANCE.log(LIE_GROUP_OPS.conjugation(g).apply(m));
      Tensor rhs = HeGroup.INSTANCE.element(g).adjoint(HeExponential.INSTANCE.log(m));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }
}
