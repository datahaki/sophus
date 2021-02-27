// code by jph
package ch.ethz.idsc.sophus.lie.so;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.sophus.lie.so3.So3TestHelper;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SoGroupElementTest extends TestCase {
  private static final LieGroup LIE_GROUP = SoGroup.INSTANCE;

  public void testBlub() {
    Tensor orth = Rodrigues.vectorExp(Tensors.vector(-0.2, 0.3, 0.1));
    Tensor matr = Rodrigues.vectorExp(Tensors.vector(+0.1, 0.2, 0.3));
    SoGroupElement.of(orth).combine(matr);
    AssertFail.of(() -> SoGroupElement.of(orth).combine(matr.add(matr)));
  }

  public void testAdjoint() {
    Tensor orth = Rodrigues.vectorExp(Tensors.vector(-0.2, 0.3, 0.1));
    SoGroupElement so3GroupElement = SoGroupElement.of(orth);
    Tensor vector = So3TestHelper.spawn_so3();
    Tensor adjoint = so3GroupElement.adjoint(vector);
    AntisymmetricMatrixQ.require(adjoint);
  }

  public void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3(); // element
      Tensor x = So3TestHelper.spawn_so3(); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(Rodrigues.INSTANCE.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(Rodrigues.INSTANCE.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  public void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    int fails = 0;
    for (int count = 0; count < 10; ++count)
      try {
        Tensor g = So3TestHelper.spawn_So3();
        Tensor m = So3TestHelper.spawn_So3();
        LieGroupElement ge = LIE_GROUP.element(g);
        Tensor lhs = Rodrigues.INSTANCE.log( //
            LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
        Tensor rhs = ge.adjoint(Rodrigues.INSTANCE.log(m)); // Ad(g).Log[m]
        Chop._10.requireClose(lhs, rhs);
      } catch (Exception exception) {
        ++fails;
      }
    assertTrue(fails < 2);
  }

  public void testCombine() {
    LieGroupElement lieGroupElement = LIE_GROUP.element(So3TestHelper.spawn_So3());
    for (int count = 0; count < 100; ++count)
      lieGroupElement = LIE_GROUP.element(lieGroupElement.combine(So3TestHelper.spawn_So3()));
  }

  public void testSimple() {
    SoGroupElement so3GroupElement = SoGroupElement.of(IdentityMatrix.of(3));
    so3GroupElement.inverse();
    AssertFail.of(() -> so3GroupElement.combine(HilbertMatrix.of(3)));
  }

  public void testDetNegFail() {
    AssertFail.of(() -> SoGroupElement.of(DiagonalMatrix.of(1, 1, -1)));
  }

  public void testDetNegCombineFail() {
    SoGroupElement so3GroupElement = SoGroupElement.of(IdentityMatrix.of(3));
    AssertFail.of(() -> so3GroupElement.combine(DiagonalMatrix.of(1, 1, -1)));
  }

  public void testFail() {
    AssertFail.of(() -> SoGroupElement.of(HilbertMatrix.of(3)));
  }

  public void testSize4Ok() {
    SoGroupElement.of(IdentityMatrix.of(4));
  }
}
