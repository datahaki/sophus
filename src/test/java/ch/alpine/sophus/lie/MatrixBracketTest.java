// code by jph
package ch.alpine.sophus.lie;

import java.util.Arrays;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.lie.r2.RotationMatrix;
import ch.alpine.tensor.red.KroneckerDelta;
import junit.framework.TestCase;

public class MatrixBracketTest extends TestCase {
  private static void _check(Tensor ad, Tensor basis) {
    JacobiIdentity.require(ad);
    int n = ad.length();
    assertEquals(n, basis.length());
    for (int c0 = 0; c0 < n; ++c0)
      for (int c1 = 0; c1 < n; ++c1) {
        Tensor mr = MatrixBracket.of(basis.get(c0), basis.get(c1));
        Tensor ar = ad.dot(UnitVector.of(n, c0)).dot(UnitVector.of(n, c1));
        assertEquals(ar.dot(basis), mr);
      }
  }

  public void testHe1Basis() {
    Tensor b0 = Array.of(l -> KroneckerDelta.of(l, Arrays.asList(0, 1)), 3, 3);
    Tensor b1 = Array.of(l -> KroneckerDelta.of(l, Arrays.asList(1, 2)), 3, 3);
    Tensor b2 = Array.of(l -> KroneckerDelta.of(l, Arrays.asList(0, 2)), 3, 3);
    Tensor basis = Tensors.of(b0, b2, b1);
    _check(new HeAlgebra(1).ad(), basis);
  }

  public void testSo3Basis() {
    Tensor basis = LeviCivitaTensor.of(3).negate();
    _check(So3Algebra.INSTANCE.ad(), basis);
  }

  public void testSe2Basis() {
    Tensor b0 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {0, 0, 0}}");
    Tensor b1 = Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 0, 0}}");
    Tensor b2 = LeviCivitaTensor.of(3).get(2).negate();
    Tensor basis = Tensors.of(b0, b1, b2);
    _check(Se2Algebra.INSTANCE.ad(), basis);
  }

  public void testSe2Matrix() {
    Tensor bx = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {0, 0, 0}}");
    Tensor by = Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 0, 0}}");
    Tensor bt = Tensors.fromString("{{0, -1, 0}, {1, 0, 0}, {0, 0, 0}}");
    assertEquals(MatrixBracket.of(bx, by), Array.zeros(3, 3));
    assertEquals(MatrixBracket.of(bt, bx), by);
    assertEquals(MatrixBracket.of(by, bt), bx);
  }

  public void testSo3Bracket() {
    Tensor so3 = LeviCivitaTensor.of(3).negate();
    assertEquals(MatrixBracket.of(so3.get(0), so3.get(1)), so3.get(2));
  }

  public void testBracketVectorFail() {
    AssertFail.of(() -> MatrixBracket.of(Tensors.empty(), Tensors.empty()));
    AssertFail.of(() -> MatrixBracket.of(Tensors.vector(1, 2), Tensors.vector(3, 4)));
  }

  public void testBracketMatrixFail() {
    Tensor x = RotationMatrix.of(RealScalar.ONE);
    Tensor y = Tensors.vector(3, 4);
    AssertFail.of(() -> MatrixBracket.of(x, y));
    AssertFail.of(() -> MatrixBracket.of(y, x));
  }

  public void testBracketAdFail() {
    AssertFail.of(() -> MatrixBracket.of(Array.zeros(2, 2, 2), Array.zeros(2, 2, 2)));
  }

  public void testBracketAdVectorFail() {
    Tensor x = Array.zeros(3, 3, 3);
    Tensor y = Tensors.vector(1, 2, 3);
    AssertFail.of(() -> MatrixBracket.of(x, y));
    AssertFail.of(() -> MatrixBracket.of(y, x));
  }

  public void testMatrixAlg() {
    assertEquals(So3Algebra.INSTANCE.ad(), LeviCivitaTensor.of(3).negate());
  }
}
