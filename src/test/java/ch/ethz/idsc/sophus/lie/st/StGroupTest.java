// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class StGroupTest extends TestCase {
  private static final Exponential LIE_EXPONENTIAL = StExponential.INSTANCE;
  private static final LieGroup LIE_GROUP = StGroup.INSTANCE;

  public void testSt1Inverse() {
    Tensor p = Tensors.fromString("{3, {6, 3}}");
    Tensor id = Tensors.fromString("{1, {0, 0}}");
    StGroupElement pE = StGroup.INSTANCE.element(p);
    StGroupElement inv = pE.inverse();
    assertEquals(inv.toCoordinate(), Tensors.fromString("{1/3, {-2, -1}}"));
    assertEquals(inv.combine(p), id);
  }

  public void testSt1Combine() {
    Tensor p = Tensors.fromString("{3, {6, 1}}");
    StGroupElement pE = StGroup.INSTANCE.element(p);
    Tensor q = Tensors.fromString("{2, {8, 5}}");
    assertEquals(pE.combine(q), Tensors.fromString("{6, {30, 16}}"));
  }

  public void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int n = 1; n < 10; ++n) {
      Tensor g = TestHelper.spawn_St(n); // element
      Tensor x = TestHelper.spawn_st(n); // vector
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
      Tensor g = TestHelper.spawn_St(n);
      Tensor m = TestHelper.spawn_St(n);
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_EXPONENTIAL.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_EXPONENTIAL.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }
}
