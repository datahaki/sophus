// code by jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class Se2CoveringExponentialTest extends TestCase {
  private static final Exponential LIE_EXPONENTIAL = Se2CoveringExponential.INSTANCE;
  private static final LieGroup LIE_GROUP = Se2CoveringGroup.INSTANCE;

  public void testSimpleXY() {
    Tensor x = Tensors.vector(3, 2, 0).unmodifiable();
    Tensor g = LIE_EXPONENTIAL.exp(x);
    ExactTensorQ.require(g);
    assertEquals(g, x);
    Tensor y = LIE_EXPONENTIAL.log(g);
    ExactTensorQ.require(y);
    assertEquals(y, x);
  }

  public void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3); // element
      Tensor x = RandomVariate.of(distribution, 3); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(LIE_EXPONENTIAL.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(LIE_EXPONENTIAL.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  public void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3); // element
      Tensor m = RandomVariate.of(distribution, 3); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_EXPONENTIAL.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_EXPONENTIAL.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  public void testSimpleLinearSubspace() {
    for (int theta = -10; theta <= 10; ++theta) {
      Tensor x = Tensors.vector(0, 0, theta).unmodifiable();
      Tensor g = Se2CoveringExponential.INSTANCE.exp(x);
      assertEquals(g, x);
      Tensor y = Se2CoveringExponential.INSTANCE.log(g);
      assertEquals(y, x);
    }
  }

  public void testQuantity() {
    Tensor xya = Tensors.fromString("{1[m], 2[m], 0.3}");
    Tensor log = LIE_EXPONENTIAL.log(xya);
    Chop._12.requireClose(log, Tensors.fromString("{1.2924887258384925[m], 1.834977451676985[m], 0.3}"));
    Tensor exp = LIE_EXPONENTIAL.exp(log);
    Chop._12.requireClose(exp, xya);
  }
}
