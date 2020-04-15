// code by jph
package ch.ethz.idsc.sophus.math;

import junit.framework.TestCase;

public class TensorMetricSquaredTest extends TestCase {
  public void testFailNull() {
    try {
      TensorMetricSquared.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
