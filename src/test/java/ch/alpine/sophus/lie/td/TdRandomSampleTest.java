// code by jph
package ch.alpine.sophus.lie.td;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class TdRandomSampleTest {
  @Test
  void testLogInv() {
    RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), 2, ExponentialDistribution.standard());
    Tensor lambda_t = RandomSample.of(rsi);
    Tensor inv = TdGroup.INSTANCE.invert(lambda_t);
    Tensor neutral = TdGroup.INSTANCE.combine(lambda_t, inv);
    Tolerance.CHOP.requireClose(neutral, UnitVector.of(3, 2));
    Tensor log1 = TdGroup.INSTANCE.exponential0().log(lambda_t);
    Tensor log2 = TdGroup.INSTANCE.exponential0().log(inv);
    Tolerance.CHOP.requireClose(log1, log2.negate());
  }

  @Disabled
  @Test
  void testAdLog() {
    for (int count = 0; count < 10; ++count) {
      RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), 2, ExponentialDistribution.standard());
      Tensor g = RandomSample.of(rsi);
      Tensor m = RandomSample.of(rsi);
      Tensor lhs = TdGroup.INSTANCE.exponential0().log(TdGroup.INSTANCE.conjugation(g).apply(m));
      Tensor rhs = TdGroup.INSTANCE.adjoint(g, TdGroup.INSTANCE.exponential0().log(m));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }
}
