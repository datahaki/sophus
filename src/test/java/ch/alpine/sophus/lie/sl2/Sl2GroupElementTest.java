// code by jph
package ch.alpine.sophus.lie.sl2;

import java.util.Random;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.sca.Chop;
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

  private static Tensor sl2_basisA() {
    Tensor b0 = Tensors.fromString("{{0, 1}, {-1, 0}}");
    Tensor b1 = Tensors.fromString("{{0, 1}, {+1, 0}}");
    Tensor b2 = Tensors.fromString("{{1, 0}, {0, -1}}");
    return Tensors.of(b0, b1, b2);
  }

  public void testBasisA() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.125);
    Tensor y = Tensors.vector(0.15, -0.08, 0.025);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(sl2_basisA());
    Tensor mx = matrixAlgebra.toMatrix(x);
    Tensor my = matrixAlgebra.toMatrix(y);
    Tensor gX = Sl2Exponential.INSTANCE.exp(mx);
    Tensor gY = Sl2Exponential.INSTANCE.exp(my);
    Tensor gZ = Sl2Exponential.INSTANCE.log(gX.dot(gY));
    Tensor az = matrixAlgebra.toVector(gZ);
    Tensor rs = matrixAlgebra.bch(6).apply(x, y);
    Chop._06.requireClose(az, rs);
  }

  public void testFailZero() {
    AssertFail.of(() -> Sl2Group.INSTANCE.element(Tensors.vector(1, 2, 0)));
  }
}
