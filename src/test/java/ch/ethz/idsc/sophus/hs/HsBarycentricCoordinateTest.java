// code by jph
package ch.ethz.idsc.sophus.hs;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import junit.framework.TestCase;

public class HsBarycentricCoordinateTest extends TestCase {
  public void testMod() {
    int modifiers = HsBarycentricCoordinate.class.getModifiers();
    assertTrue(Modifier.isFinal(modifiers));
  }

  public void testCustomNullFail() {
    try {
      HsBarycentricCoordinate.custom(RnManifold.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
