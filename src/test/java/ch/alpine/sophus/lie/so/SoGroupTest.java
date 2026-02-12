// code by jph
package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.sca.Chop;

class SoGroupTest {
  private static final LieGroup LIE_GROUP = SoGroup.INSTANCE;

  @Disabled
  @Test
  void testBlub() {
    Tensor orth = Rodrigues.vectorExp(Tensors.vector(-0.2, 0.3, 0.1));
    Tensor matr = Rodrigues.vectorExp(Tensors.vector(+0.1, 0.2, 0.3));
    SoGroup.INSTANCE.combine(orth, matr);
    assertThrows(Exception.class, () -> SoGroup.INSTANCE.combine(orth, matr.add(matr)));
  }

  @Test
  void testAdjoint() {
    Tensor orth = Rodrigues.vectorExp(Tensors.vector(-0.2, 0.3, 0.1));
    // SoGroupElement so3GroupElement = SoGroupElement.of(orth);
    Tensor vector = So3TestHelper.spawn_so3();
    Tensor adjoint = So3Group.INSTANCE.adjoint(orth, vector);
    AntisymmetricMatrixQ.INSTANCE.requireMember(adjoint);
  }

  @Test
  void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3(); // element
      Tensor x = So3TestHelper.spawn_so3(); // vector
      Tensor lhs = LIE_GROUP.combine(g, Rodrigues.INSTANCE.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.combine(Rodrigues.INSTANCE.exp(LIE_GROUP.adjoint(g, x)), g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    RandomGenerator random = new Random(3);
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3(random);
      Tensor m = So3TestHelper.spawn_So3(random);
      Tensor lhs = Rodrigues.INSTANCE.log(LIE_GROUP.combine(LIE_GROUP.combine(g, m), LIE_GROUP.invert(g))); // Log[g.m.g^-1]
      Tensor rhs = LIE_GROUP.adjoint(g, Rodrigues.INSTANCE.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testCombine() {
    Tensor p = So3TestHelper.spawn_So3();
    for (int count = 0; count < 100; ++count)
      p = LIE_GROUP.combine(p, So3TestHelper.spawn_So3());
  }

  @Test
  void testDet1() {
    Tensor nondet = DiagonalMatrix.of(1, 1, -1);
    assertEquals(Det.of(nondet), RealScalar.ONE.negate());
    assertFalse(SoGroup.INSTANCE.isPointQ().isMember(nondet));
  }

  @Disabled
  @Test
  void testSimple67() {
    assertThrows(Exception.class, () -> SoGroup.INSTANCE.combine(IdentityMatrix.of(3), HilbertMatrix.of(3)));
  }

  @Test
  void testDetNegFail33() {
    assertThrows(Exception.class, () -> SoGroup.INSTANCE.isPointQ().requireMember(DiagonalMatrix.of(1, 1, -1)));
  }

  @Disabled
  @Test
  void testDetNegCombineFail() {
    assertThrows(Exception.class, () -> SoGroup.INSTANCE.combine(IdentityMatrix.of(3), DiagonalMatrix.of(1, 1, -1)));
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> SoGroup.INSTANCE.isPointQ().requireMember(HilbertMatrix.of(3)));
  }

  @Test
  void testSize4Ok() {
    SoGroup.INSTANCE.isPointQ().requireMember(IdentityMatrix.of(4));
  }

  @Test
  void testDetNegFail() {
    assertThrows(Exception.class, () -> SoGroup.INSTANCE.isPointQ().requireMember(DiagonalMatrix.of(1, 1, -1)));
  }

  @Test
  void testSimple() {
    RandomGenerator random = new Random(1);
    for (int n = 2; n < 10; ++n) {
      Tensor p = new SoNGroup(n).randomSample(random);
      Tensor q = new SoNGroup(n).randomSample(random);
      Exponential exponential = SoGroup.INSTANCE.exponential(p);
      Tensor vp = exponential.log(q);
      TSoMemberQ.INSTANCE.requireMember(vp);
      Tensor qr = exponential.exp(vp);
      Chop._06.requireClose(q, qr);
      // Tensor log = exponential.vectorLog(q);
      // assertEquals(log.length(), n * (n - 1) / 2);
    }
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> SoGroup.INSTANCE.isPointQ().requireMember(null));
  }
}
