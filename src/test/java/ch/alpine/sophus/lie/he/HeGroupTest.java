// code by jph
package ch.alpine.sophus.lie.he;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

class HeGroupTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(HeGroup.INSTANCE);
  private static final RandomSampleInterface RSI = new HeRandomSample(2, UniformDistribution.of(Clips.absolute(10)));

  @Test
  void testSimple() {
    Tensor p = Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, 7}");
    Tensor q = Tensors.fromString("{{-1, 6, 2}, {-3, -2, 1}, -4}");
    Tensor actual = HeGroup.INSTANCE.split(p, q, RationalScalar.HALF);
    ExactTensorQ.require(actual);
    Tensor expect = Tensors.fromString("{{0, 4, 5/2}, {1/2, 3/2, 7/2}, 21/8}");
    assertEquals(actual, expect);
  }

  @Test
  void testExpLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = RandomSample.of(RSI);
      Tensor xyz = HeGroup.INSTANCE.exp(inp);
      Tensor uvw = HeGroup.INSTANCE.log(xyz);
      Tolerance.CHOP.requireClose(inp, uvw);
    }
  }

  @Test
  void testLogExp() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = RandomSample.of(RSI);
      Tensor uvw = HeGroup.INSTANCE.log(inp);
      Tensor xyz = HeGroup.INSTANCE.exp(uvw);
      Tolerance.CHOP.requireClose(inp, xyz);
    }
  }

  @Test
  void testAdLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomSample.of(RSI);
      Tensor m = RandomSample.of(RSI);
      Tensor lhs = HeGroup.INSTANCE.log(LIE_GROUP_OPS.conjugation(g).apply(m));
      Tensor rhs = HeGroup.INSTANCE.element(g).adjoint(HeGroup.INSTANCE.log(m));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> HeGroup.INSTANCE.element(null));
  }
}
