// code by jph
package ch.ethz.idsc.sophus.itp;

import junit.framework.TestCase;

public class StandardVariogramTest extends TestCase {
  public void testSimple() {
    try {
      PowerVariogram.of(1, 2);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
