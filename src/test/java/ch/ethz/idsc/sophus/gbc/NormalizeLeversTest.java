// code by jph
package ch.ethz.idsc.sophus.gbc;

import junit.framework.TestCase;

public class NormalizeLeversTest extends TestCase {
  public void testSimple() {
    try {
      new NormalizeLevers(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
