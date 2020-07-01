// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import junit.framework.TestCase;

public class InverseDistanceCoordinateTest extends TestCase {
  public void testCustomNullFail() {
    try {
      InverseDistanceCoordinate.custom(RnManifold.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
