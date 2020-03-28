// code by jph
package ch.ethz.idsc.sophus.hs;

import junit.framework.TestCase;

public class BiinvariantMeanDefectTest extends TestCase {
  public void testNullFail() {
    try {
      BiinvariantMeanDefect.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
