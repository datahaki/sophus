// code by jph
package ch.alpine.sophus.lie.he;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class HeGroupTest {
  private static final RandomSampleInterface RSI = new HeRandomSample(2, UniformDistribution.of(Clips.absolute(10)));

  @Test
  void testSimple1() {
    Tensor p = Tensors.fromString("{1, 2, 3, 4, 5, 6, 7}");
    Tensor q = Tensors.fromString("{-1, 6, 2, -3, -2, 1, -4}");
    Tensor actual = HeGroup.INSTANCE.split(p, q, RationalScalar.HALF);
    ExactTensorQ.require(actual);
    Tensor expect = Tensors.fromString("{0, 4, 5/2, 1/2, 3/2, 7/2, 21/8}");
    assertEquals(actual, expect);
  }

  @Test
  void testExpLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = RandomSample.of(RSI);
      Tensor xyz = HeGroup.INSTANCE.exponential0().exp(inp);
      Tensor uvw = HeGroup.INSTANCE.exponential0().log(xyz);
      Tolerance.CHOP.requireClose(inp, uvw);
    }
  }

  @Test
  void testLogExp() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = RandomSample.of(RSI);
      Tensor uvw = HeGroup.INSTANCE.exponential0().log(inp);
      Tensor xyz = HeGroup.INSTANCE.exponential0().exp(uvw);
      Tolerance.CHOP.requireClose(inp, xyz);
    }
  }

  @Test
  void testAdLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomSample.of(RSI);
      Tensor m = RandomSample.of(RSI);
      Tensor lhs = HeGroup.INSTANCE.exponential0().log(HeGroup.INSTANCE.conjugation(g).apply(m));
      Tensor rhs = HeGroup.INSTANCE.adjoint(g, HeGroup.INSTANCE.exponential0().log(m));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }

  private static final LieGroup LIE_GROUP = HeGroup.INSTANCE;

  @Test
  void testInverse() {
    Tensor et = Tensors.fromString("{0, 0, 0, 0, 0}");
    Tensor at = Tensors.fromString("{1, 2, 3, 4, 5}");
    Tensor b = HeGroup.INSTANCE.invert(at);
    Tensor result = HeGroup.INSTANCE.combine(b, at);
    assertEquals(result, et);
  }

  @Test
  void testCombine() {
    Tensor a_t = Tensors.fromString("{1, 2, 3, 4, 5}");
    // HeGroupElement a = new HeGroupElement(a_t);
    Tensor b_t = Tensors.fromString("{6, 7, 8, 9, 10}");
    Tensor ab_t = HeGroup.INSTANCE.combine(a_t, b_t);
    ExactTensorQ.require(ab_t);
    assertEquals(ab_t, Tensors.fromString("{7, 9, 11, 13, 41}"));
    Tensor a_r = HeGroup.INSTANCE.combine(ab_t, HeGroup.INSTANCE.invert(b_t));
    assertEquals(a_r, a_t);
    Tensor b_r = HeGroup.INSTANCE.diffOp(a_t).apply(ab_t);
    assertEquals(b_t, b_r);
  }

  @Test
  void testAdjoint1() {
    Tensor a_t = Tensors.fromString("{1, 2, 3, 4, 5}");
    Tensor b_t = Tensors.fromString("{6, 7, 0, 0, 10}");
    Tensor tensor = HeGroup.INSTANCE.adjoint(a_t, b_t);
    assertEquals(tensor, Tensors.fromString("{6, 7, 0, 0, -3*6-4*7+10}"));
    ExactTensorQ.require(tensor);
  }

  @Test
  void testAdjoint2() {
    Tensor a_t = Tensors.fromString("{1, 2, 3, 4, 5}");
    Tensor b_t = Tensors.fromString("{0, 0, 6, 7, 9}");
    Tensor tensor = HeGroup.INSTANCE.adjoint(a_t, b_t);
    assertEquals(tensor, Tensors.fromString("{0, 0, 6, 7, 1*6+2*7+9}"));
    ExactTensorQ.require(tensor);
  }

  @Test
  void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new HeRandomSample(n, UniformDistribution.of(Clips.absolute(10)));
      Tensor g = RandomSample.of(rsi);
      Tensor x = RandomSample.of(rsi);
      Tensor lhs = LIE_GROUP.exponential(g).exp(x); // g.Exp[x]
      Tensor rhs = LIE_GROUP.combine(LIE_GROUP.exponential0().exp(LIE_GROUP.adjoint(g, x)), g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new HeRandomSample(n, UniformDistribution.of(Clips.absolute(10)));
      Tensor g = RandomSample.of(rsi);
      Tensor m = RandomSample.of(rsi);
      Tensor lhs = LIE_GROUP.exponential0().log( //
          LIE_GROUP.combine(LIE_GROUP.combine(g, m), LIE_GROUP.invert(g))); // Log[g.m.g^-1]
      Tensor rhs = LIE_GROUP.adjoint(g, LIE_GROUP.exponential0().log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testAdInverse() {
    RandomSampleInterface rsi = new HeRandomSample(2, UniformDistribution.of(Clips.absolute(10)));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomSample.of(rsi);
      Tensor lhs = RandomSample.of(rsi);
      Tensor rhs = HeGroup.INSTANCE.adjoint(HeGroup.INSTANCE.invert(g), HeGroup.INSTANCE.adjoint(g, lhs));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }
  // @Test
  // void testFail() {
  // assertThrows(Exception.class, () -> new HeGroupElement(Tensors.of(HilbertMatrix.of(3), Tensors.vector(1, 2, 3), RealScalar.ONE)));
  // assertThrows(Exception.class, () -> new HeGroupElement(Tensors.of(Tensors.vector(1, 2, 3), HilbertMatrix.of(3), RealScalar.ONE)));
  // }

  @Test
  void testDlNullFail() {
    Tensor a_t = Tensors.fromString("{1, 2, 3, 4, 5}");
    assertThrows(Exception.class, () -> HeGroup.INSTANCE.dL(a_t, null));
  }
}
