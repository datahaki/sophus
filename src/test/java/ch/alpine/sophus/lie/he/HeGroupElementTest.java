// code by jph
package ch.alpine.sophus.lie.he;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class HeGroupElementTest {
  private static final Exponential LIE_EXPONENTIAL = HeExponential.INSTANCE;
  private static final LieGroup LIE_GROUP = HeGroup.INSTANCE;

  @Test
  public void testInverse() {
    Tensor et = Tensors.fromString("{{0, 0}, {0, 0}, 0}");
    Tensor at = Tensors.fromString("{{1, 2}, {3, 4}, 5}");
    HeGroupElement a = new HeGroupElement(at);
    HeGroupElement b = a.inverse();
    Tensor result = b.combine(at);
    assertEquals(result, et);
  }

  @Test
  public void testCombine() {
    Tensor a_t = Tensors.fromString("{{1, 2}, {3, 4}, 5}");
    HeGroupElement a = new HeGroupElement(a_t);
    Tensor b_t = Tensors.fromString("{{6, 7}, {8, 9}, 10}");
    Tensor ab_t = a.combine(b_t);
    ExactTensorQ.require(ab_t);
    assertEquals(ab_t, Tensors.fromString("{{7, 9}, {11, 13}, 41}"));
    HeGroupElement ab = new HeGroupElement(ab_t);
    Tensor a_r = ab.combine(new HeGroupElement(b_t).inverse().toCoordinate());
    assertEquals(a_r, a_t);
    Tensor b_r = a.inverse().combine(ab.toCoordinate());
    assertEquals(b_t, b_r);
  }

  @Test
  public void testAdjoint1() {
    Tensor a_t = Tensors.fromString("{{1, 2}, {3, 4}, 5}");
    Tensor b_t = Tensors.fromString("{{6, 7}, {0, 0}, 10}");
    HeGroupElement a = new HeGroupElement(a_t);
    Tensor tensor = a.adjoint(b_t);
    assertEquals(tensor, Tensors.fromString("{{6, 7}, {0, 0}, -3*6-4*7+10}"));
    ExactTensorQ.require(tensor);
  }

  @Test
  public void testAdjoint2() {
    Tensor a_t = Tensors.fromString("{{1, 2}, {3, 4}, 5}");
    Tensor b_t = Tensors.fromString("{{0, 0}, {6, 7}, 9}");
    HeGroupElement a = new HeGroupElement(a_t);
    Tensor tensor = a.adjoint(b_t);
    assertEquals(tensor, Tensors.fromString("{{0, 0}, {6, 7}, 1*6+2*7+9}"));
    ExactTensorQ.require(tensor);
  }

  @Test
  public void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new HeRandomSample(n, UniformDistribution.of(Clips.absolute(10)));
      Tensor g = RandomSample.of(rsi);
      Tensor x = RandomSample.of(rsi);
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(LIE_EXPONENTIAL.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(LIE_EXPONENTIAL.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  public void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new HeRandomSample(n, UniformDistribution.of(Clips.absolute(10)));
      Tensor g = RandomSample.of(rsi);
      Tensor m = RandomSample.of(rsi);
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_EXPONENTIAL.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_EXPONENTIAL.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  public void testAdInverse() {
    RandomSampleInterface rsi = new HeRandomSample(2, UniformDistribution.of(Clips.absolute(10)));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomSample.of(rsi);
      Tensor lhs = RandomSample.of(rsi);
      Tensor rhs = HeGroup.INSTANCE.element(g).inverse().adjoint(HeGroup.INSTANCE.element(g).adjoint(lhs));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> new HeGroupElement(Tensors.of(HilbertMatrix.of(3), Tensors.vector(1, 2, 3), RealScalar.ONE)));
    assertThrows(Exception.class, () -> new HeGroupElement(Tensors.of(Tensors.vector(1, 2, 3), HilbertMatrix.of(3), RealScalar.ONE)));
  }

  @Test
  public void testDlNullFail() {
    Tensor a_t = Tensors.fromString("{{1, 2}, {3, 4}, 5}");
    HeGroupElement a = new HeGroupElement(a_t);
    assertThrows(Exception.class, () -> a.dL(null));
  }
}
