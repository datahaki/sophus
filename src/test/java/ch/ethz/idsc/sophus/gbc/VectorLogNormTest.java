// code by jph
package ch.ethz.idsc.sophus.gbc;

import junit.framework.TestCase;

public class VectorLogNormTest extends TestCase {
  public void testSimple() {
    try {
      new LeversWeighting(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
