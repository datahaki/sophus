// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3GroupElementTest extends TestCase {
  private static final Exponential LIE_EXPONENTIAL = So3Exponential.INSTANCE;
  private static final LieGroup LIE_GROUP = So3Group.INSTANCE;

  public void testBlub() {
    Tensor orth = LIE_EXPONENTIAL.exp(Tensors.vector(-0.2, 0.3, 0.1));
    Tensor matr = LIE_EXPONENTIAL.exp(Tensors.vector(+0.1, 0.2, 0.3));
    So3GroupElement.of(orth).combine(matr);
    try {
      So3GroupElement.of(orth).combine(matr.add(matr));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testAdjoint() {
    Tensor orth = So3Exponential.INSTANCE.exp(Tensors.vector(-0.2, 0.3, 0.1));
    So3GroupElement so3GroupElement = So3GroupElement.of(orth);
    Tensor vector = Tensors.vector(1, 2, 3);
    Tensor adjoint = so3GroupElement.adjoint(vector);
    Chop._12.requireClose(orth.dot(vector), adjoint);
  }

  public void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int count = 0; count < 10; ++count) {
      Tensor g = TestHelper.spawn_So3(); // element
      Tensor x = TestHelper.spawn_so3(); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(LIE_EXPONENTIAL.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(LIE_EXPONENTIAL.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  public void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    for (int count = 0; count < 10; ++count) {
      Tensor g = TestHelper.spawn_So3();
      Tensor m = TestHelper.spawn_So3();
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_EXPONENTIAL.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_EXPONENTIAL.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  public void testCombine() {
    LieGroupElement lieGroupElement = LIE_GROUP.element(TestHelper.spawn_So3());
    for (int count = 0; count < 100; ++count)
      lieGroupElement = LIE_GROUP.element(lieGroupElement.combine(TestHelper.spawn_So3()));
  }

  public void testSimple() {
    So3GroupElement so3GroupElement = So3GroupElement.of(IdentityMatrix.of(3));
    so3GroupElement.inverse();
    try {
      so3GroupElement.combine(HilbertMatrix.of(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testDetNegFail() {
    try {
      So3GroupElement.of(DiagonalMatrix.of(1, 1, -1));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testDetNegCombineFail() {
    So3GroupElement so3GroupElement = So3GroupElement.of(IdentityMatrix.of(3));
    try {
      so3GroupElement.combine(DiagonalMatrix.of(1, 1, -1));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFail() {
    try {
      So3GroupElement.of(HilbertMatrix.of(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testSizeFail() {
    try {
      So3GroupElement.of(IdentityMatrix.of(4));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
