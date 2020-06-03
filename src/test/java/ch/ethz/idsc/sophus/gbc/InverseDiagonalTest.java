// code by jph
package ch.ethz.idsc.sophus.gbc;

import junit.framework.TestCase;

public class InverseDiagonalTest extends TestCase {
  public void testNullFail() {
    try {
      InverseDiagonal.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
