// code by jph
package ch.alpine.sophus.lie.so;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.sophus.lie.so3.So3TestHelper;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.sca.Chop;

public class SoGroupElementTest {
  private static final LieGroup LIE_GROUP = SoGroup.INSTANCE;

  @Test
  public void testBlub() {
    Tensor orth = Rodrigues.vectorExp(Tensors.vector(-0.2, 0.3, 0.1));
    Tensor matr = Rodrigues.vectorExp(Tensors.vector(+0.1, 0.2, 0.3));
    SoGroupElement.of(orth).combine(matr);
    AssertFail.of(() -> SoGroupElement.of(orth).combine(matr.add(matr)));
  }

  @Test
  public void testAdjoint() {
    Tensor orth = Rodrigues.vectorExp(Tensors.vector(-0.2, 0.3, 0.1));
    SoGroupElement so3GroupElement = SoGroupElement.of(orth);
    Tensor vector = So3TestHelper.spawn_so3();
    Tensor adjoint = so3GroupElement.adjoint(vector);
    AntisymmetricMatrixQ.require(adjoint);
  }

  @Test
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

  @Test
  public void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    Random random = new Random(3);
    for (int count = 0; count < 10; ++count) {
      Tensor g = So3TestHelper.spawn_So3(random);
      Tensor m = So3TestHelper.spawn_So3(random);
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = Rodrigues.INSTANCE.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(Rodrigues.INSTANCE.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  public void testCombine() {
    LieGroupElement lieGroupElement = LIE_GROUP.element(So3TestHelper.spawn_So3());
    for (int count = 0; count < 100; ++count)
      lieGroupElement = LIE_GROUP.element(lieGroupElement.combine(So3TestHelper.spawn_So3()));
  }

  @Test
  public void testSimple() {
    SoGroupElement so3GroupElement = SoGroupElement.of(IdentityMatrix.of(3));
    so3GroupElement.inverse();
    AssertFail.of(() -> so3GroupElement.combine(HilbertMatrix.of(3)));
  }

  @Test
  public void testDetNegFail() {
    AssertFail.of(() -> SoGroupElement.of(DiagonalMatrix.of(1, 1, -1)));
  }

  @Test
  public void testDetNegCombineFail() {
    SoGroupElement so3GroupElement = SoGroupElement.of(IdentityMatrix.of(3));
    AssertFail.of(() -> so3GroupElement.combine(DiagonalMatrix.of(1, 1, -1)));
  }

  @Test
  public void testFail() {
    AssertFail.of(() -> SoGroupElement.of(HilbertMatrix.of(3)));
  }

  @Test
  public void testSize4Ok() {
    SoGroupElement.of(IdentityMatrix.of(4));
  }
}
