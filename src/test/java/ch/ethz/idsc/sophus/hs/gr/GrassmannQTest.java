// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class GrassmannQTest extends TestCase {
  public void testSimple() {
    Tensor matrix = Tensors.fromString("{{1, 0}, {2, 3}}");
    assertFalse(GrassmannQ.of(matrix));
    try {
      GrassmannQ.require(matrix);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testChop() {
    Tensor matrix = Tensors.fromString("{{1, 0}, {2, 3}}");
    assertFalse(GrassmannQ.of(matrix, Tolerance.CHOP));
    try {
      GrassmannQ.require(matrix, Tolerance.CHOP);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
