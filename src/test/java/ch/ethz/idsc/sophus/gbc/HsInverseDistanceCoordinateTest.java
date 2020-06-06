// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import junit.framework.TestCase;

public class HsInverseDistanceCoordinateTest extends TestCase {
  public void testMod() {
    int modifiers = HsInverseDistanceCoordinate.class.getModifiers();
    assertTrue(Modifier.isFinal(modifiers));
  }

  public void testNullWeightingFail() {
    try {
      HsInverseDistanceCoordinate.custom(SnManifold.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
