// code by jph
package ch.ethz.idsc.sophus.hs;

import junit.framework.TestCase;

public class MeanDefectTest extends TestCase {
  public void testNullFail() {
    try {
      new MeanDefect(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
