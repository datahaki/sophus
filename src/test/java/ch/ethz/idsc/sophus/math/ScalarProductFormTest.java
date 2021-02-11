// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class ScalarProductFormTest extends TestCase {
  public void testSimple() {
    assertEquals(ScalarProductForm.of(0, 3), DiagonalMatrix.of(-1, -1, -1));
    assertEquals(ScalarProductForm.of(3, 0), IdentityMatrix.of(3));
    assertEquals(ScalarProductForm.of(1, 2), DiagonalMatrix.of(+1, -1, -1));
    assertEquals(ScalarProductForm.of(2, 1), DiagonalMatrix.of(+1, +1, -1));
  }
}
