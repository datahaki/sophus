// code by jph
package ch.ethz.idsc.sophus.math.id;

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
