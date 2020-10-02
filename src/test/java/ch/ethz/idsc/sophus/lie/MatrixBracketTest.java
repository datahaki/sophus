// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.lie.LeviCivitaTensor;
import ch.ethz.idsc.tensor.lie.r2.RotationMatrix;
import junit.framework.TestCase;

public class MatrixBracketTest extends TestCase {
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
}
