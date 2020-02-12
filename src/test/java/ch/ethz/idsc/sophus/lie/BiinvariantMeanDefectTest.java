// code by jph
package ch.ethz.idsc.sophus.lie;

import junit.framework.TestCase;

public class BiinvariantMeanDefectTest extends TestCase {
  public void testNullFail() {
    try {
      new BiinvariantMeanDefect(null, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
