// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HeGroupElementTest extends TestCase {
  private static final Exponential LIE_EXPONENTIAL = HeExponential.INSTANCE;
  private static final LieGroup LIE_GROUP = HeGroup.INSTANCE;

  public void testInverse() {
    Tensor et = Tensors.fromString("{{0, 0}, {0, 0}, 0}");
    Tensor at = Tensors.fromString("{{1, 2}, {3, 4}, 5}");
    HeGroupElement a = new HeGroupElement(at);
    HeGroupElement b = a.inverse();
    Tensor result = b.combine(at);
    assertEquals(result, et);
  }

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

  public void testAdjoint1() {
    Tensor a_t = Tensors.fromString("{{1, 2}, {3, 4}, 5}");
    Tensor b_t = Tensors.fromString("{{6, 7}, {0, 0}, 10}");
    HeGroupElement a = new HeGroupElement(a_t);
    Tensor tensor = a.adjoint(b_t);
    assertEquals(tensor, Tensors.fromString("{{6, 7}, {0, 0}, -3*6-4*7+10}"));
    ExactTensorQ.require(tensor);
  }

  public void testAdjoint2() {
    Tensor a_t = Tensors.fromString("{{1, 2}, {3, 4}, 5}");
    Tensor b_t = Tensors.fromString("{{0, 0}, {6, 7}, 9}");
    HeGroupElement a = new HeGroupElement(a_t);
    Tensor tensor = a.adjoint(b_t);
    assertEquals(tensor, Tensors.fromString("{{0, 0}, {6, 7}, 1*6+2*7+9}"));
    ExactTensorQ.require(tensor);
  }

  public void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int n = 1; n < 10; ++n) {
      Tensor g = TestHelper.spawn_He(n); // element
      Tensor x = TestHelper.spawn_He(n); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(LIE_EXPONENTIAL.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(LIE_EXPONENTIAL.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  public void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    for (int n = 1; n < 10; ++n) {
      Tensor g = TestHelper.spawn_He(n); // element
      Tensor m = TestHelper.spawn_He(n); // element
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_EXPONENTIAL.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_EXPONENTIAL.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  public void testAdInverse() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = TestHelper.spawn_He(2);
      Tensor lhs = TestHelper.spawn_he(2);
      Tensor rhs = HeGroup.INSTANCE.element(g).inverse().adjoint(HeGroup.INSTANCE.element(g).adjoint(lhs));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }

  public void testFail() {
    try {
      new HeGroupElement(Tensors.of(HilbertMatrix.of(3), Tensors.vector(1, 2, 3), RealScalar.ONE));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      new HeGroupElement(Tensors.of(Tensors.vector(1, 2, 3), HilbertMatrix.of(3), RealScalar.ONE));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
