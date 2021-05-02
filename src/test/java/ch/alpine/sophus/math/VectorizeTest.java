// code by jph
package ch.alpine.sophus.math;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.num.Pi;
import junit.framework.TestCase;

public class VectorizeTest extends TestCase {
  public void testEmpty() {
    assertEquals(Vectorize.of(Tensors.vector(), +0), Tensors.empty());
    assertEquals(Vectorize.of(Tensors.vector(), -1), Tensors.empty());
  }

  public void testMatrixN1() {
    Tensor matrix = Tensors.fromString("{{1,2,3}, {4,5,6}, {7,8,9}}");
    assertEquals(Vectorize.of(matrix, 1), Tensors.fromString("{1,2, 4,5,6, 7,8,9}"));
    assertEquals(Vectorize.of(matrix, 0), Tensors.fromString("{1, 4,5, 7,8,9}"));
    assertEquals(Vectorize.of(matrix, -1), Tensors.fromString("{4, 7,8 }"));
  }

  public void testVectorFail() {
    AssertFail.of(() -> Vectorize.of(Tensors.vector(1, 2, 3), 0));
    AssertFail.of(() -> Vectorize.of(Tensors.vector(1, 2, 3), -1));
  }

  public void testScalarFail() {
    AssertFail.of(() -> Vectorize.of(Pi.VALUE, 0));
    AssertFail.of(() -> Vectorize.of(Pi.VALUE, -1));
  }
}
