// code by jph
package ch.alpine.sophus.lie.sl;

import java.util.Random;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class Sl2GroupElementTest extends TestCase {
  private static Tensor adjoint(LieGroupElement lieGroupElement) {
    return Tensor.of(IdentityMatrix.of(3).stream().map(lieGroupElement::adjoint));
  }

  public void testSimple() {
    Tensor vector = Tensors.vector(-2, 7, 3);
    Sl2GroupElement sl2GroupElement = Sl2Group.INSTANCE.element(vector);
    Sl2GroupElement inverse = sl2GroupElement.inverse();
    assertEquals(inverse.toCoordinate(), Tensors.fromString("{2/3, -7/3, 1/3}"));
    assertEquals(inverse.combine(vector), UnitVector.of(3, 2));
  }

  public void testAdjointId() {
    Sl2GroupElement sl2GroupElement = Sl2Group.INSTANCE.element(Tensors.vector(0, 0, 1));
    Tensor matrix = adjoint(sl2GroupElement);
    ExactTensorQ.require(matrix);
    assertEquals(matrix, IdentityMatrix.of(3));
  }

  public void testAdjointCombine() {
    Random random = new Random(3);
    for (int count = 0; count < 10; ++count) {
      Tensor a = TestHelper.spawn_Sl2(random);
      Sl2GroupElement ga = Sl2Group.INSTANCE.element(a);
      Tensor b = TestHelper.spawn_Sl2(random);
      Sl2GroupElement gb = Sl2Group.INSTANCE.element(b);
      Sl2GroupElement gab = Sl2Group.INSTANCE.element(ga.combine(b));
      Tensor matrix = adjoint(gab);
      ExactTensorQ.require(matrix);
      Tensor Ad_a = adjoint(ga);
      Tensor Ad_b = adjoint(gb);
      assertEquals(matrix, Ad_b.dot(Ad_a));
    }
  }

  public void testFailZero() {
    AssertFail.of(() -> Sl2Group.INSTANCE.element(Tensors.vector(1, 2, 0)));
  }
}
