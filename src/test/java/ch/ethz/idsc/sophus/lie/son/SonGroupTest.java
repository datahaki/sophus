// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class SonGroupTest extends TestCase {
  public void testDetNegFail() {
    try {
      SonGroup.INSTANCE.element(DiagonalMatrix.of(1, 1, -1));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFormat4Ok() {
    SonGroup.INSTANCE.element(IdentityMatrix.of(4));
  }

  public void testNullFail() {
    try {
      SonGroup.INSTANCE.element(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
