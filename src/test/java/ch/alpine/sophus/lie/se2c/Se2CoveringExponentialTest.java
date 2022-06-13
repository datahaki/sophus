// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class Se2CoveringExponentialTest {
  private static final LieGroup LIE_GROUP = Se2CoveringGroup.INSTANCE;

  @Test
  void testSimpleXY() {
    Tensor x = Tensors.vector(3, 2, 0).unmodifiable();
    Tensor g = LIE_GROUP.exp(x);
    ExactTensorQ.require(g);
    assertEquals(g, x);
    Tensor y = LIE_GROUP.log(g);
    ExactTensorQ.require(y);
    assertEquals(y, x);
  }

  @Test
  void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3); // element
      Tensor x = RandomVariate.of(distribution, 3); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(LIE_GROUP.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(LIE_GROUP.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, random, 3); // element
      Tensor m = RandomVariate.of(distribution, random, 3); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_GROUP.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_GROUP.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testSimpleLinearSubspace() {
    for (int theta = -10; theta <= 10; ++theta) {
      Tensor x = Tensors.vector(0, 0, theta).unmodifiable();
      Tensor g = Se2CoveringGroup.INSTANCE.exp(x);
      assertEquals(g, x);
      Tensor y = Se2CoveringGroup.INSTANCE.log(g);
      assertEquals(y, x);
    }
  }

  @Test
  void testQuantity() {
    Tensor xya = Tensors.fromString("{1[m], 2[m], 0.3}");
    Tensor log = LIE_GROUP.log(xya);
    Chop._12.requireClose(log, Tensors.fromString("{1.2924887258384925[m], 1.834977451676985[m], 0.3}"));
    Tensor exp = LIE_GROUP.exp(log);
    Chop._12.requireClose(exp, xya);
  }
}
