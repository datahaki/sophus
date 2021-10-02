// code by jph
package ch.alpine.sophus.math;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.num.Pi;
import junit.framework.TestCase;

public class LowerVectorizeTest extends TestCase {
  public void testEmpty() {
    AssertFail.of(() -> LowerVectorize.of(Tensors.vector(), +0));
    AssertFail.of(() -> LowerVectorize.of(Tensors.vector(), -1));
  }

  public void testMatrixN1() {
    Tensor matrix = Tensors.fromString("{{1,2,3}, {4,5,6}, {7,8,9}}");
    assertEquals(LowerVectorize.of(matrix, 1), Tensors.fromString("{1,2, 4,5,6, 7,8,9}"));
    assertEquals(LowerVectorize.of(matrix, 0), Tensors.fromString("{1, 4,5, 7,8,9}"));
    assertEquals(LowerVectorize.of(matrix, -1), Tensors.fromString("{4, 7,8 }"));
  }

  public void testScalarFail() {
    AssertFail.of(() -> LowerVectorize.of(Pi.VALUE, 0));
    AssertFail.of(() -> LowerVectorize.of(Pi.VALUE, -1));
  }

  public void testVectorFail() {
    AssertFail.of(() -> LowerVectorize.of(Tensors.vector(1, 2, 3), 0));
    AssertFail.of(() -> LowerVectorize.of(Tensors.vector(1, 2, 3), -1));
  }

  public void testRank3Fail() {
    AssertFail.of(() -> LowerVectorize.of(LeviCivitaTensor.of(3), 0));
  }
}
