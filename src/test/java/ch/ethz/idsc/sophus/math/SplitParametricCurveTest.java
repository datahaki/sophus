// code by jph
package ch.ethz.idsc.sophus.math;

import junit.framework.TestCase;

public class SplitParametricCurveTest extends TestCase {
  public void testNullFail() {
    try {
      SplitParametricCurve.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
