// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class So3GroupTest extends TestCase {
  public void testDetNegFail() {
    try {
      So3Group.INSTANCE.element(DiagonalMatrix.of(1, 1, -1));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFormatFail() {
    try {
      So3Group.INSTANCE.element(IdentityMatrix.of(4));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNullFail() {
    try {
      So3Group.INSTANCE.element(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
