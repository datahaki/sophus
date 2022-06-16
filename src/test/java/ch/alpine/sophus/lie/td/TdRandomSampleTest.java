// code by jph
package ch.alpine.sophus.lie.td;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class TdRandomSampleTest {
  @Test
  void testLogInv() {
    RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), 2, ExponentialDistribution.standard());
    Tensor lambda_t = RandomSample.of(rsi);
    TdGroupElement tdGroupElement = TdGroup.INSTANCE.element(lambda_t);
    Tensor inv = tdGroupElement.inverse().toCoordinate();
    Tensor neutral = tdGroupElement.combine(inv);
    Tolerance.CHOP.requireClose(neutral, UnitVector.of(3, 2));
    Tensor log1 = TdGroup.INSTANCE.log(lambda_t);
    Tensor log2 = TdGroup.INSTANCE.log(inv);
    Tolerance.CHOP.requireClose(log1, log2.negate());
  }

  @Test
  void testAdLog() {
    LieGroupOps LIE_GROUP_OPS = new LieGroupOps(TdGroup.INSTANCE);
    for (int count = 0; count < 10; ++count) {
      RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), 2, ExponentialDistribution.standard());
      Tensor g = RandomSample.of(rsi);
      Tensor m = RandomSample.of(rsi);
      Tensor lhs = TdGroup.INSTANCE.log(LIE_GROUP_OPS.conjugation(g).apply(m));
      Tensor rhs = TdGroup.INSTANCE.element(g).adjoint(TdGroup.INSTANCE.log(m));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }
}
