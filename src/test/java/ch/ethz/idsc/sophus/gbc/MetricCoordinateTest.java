// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import junit.framework.TestCase;

public class MetricCoordinateTest extends TestCase {
  public void testCustomNullFail() {
    try {
      MetricCoordinate.custom(RnManifold.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
