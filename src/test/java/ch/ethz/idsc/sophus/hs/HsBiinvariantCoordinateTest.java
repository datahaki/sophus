// code by jph
package ch.ethz.idsc.sophus.hs;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import junit.framework.TestCase;

public class HsBiinvariantCoordinateTest extends TestCase {
  public void testMod() {
    int modifiers = HsBiinvariantCoordinate.class.getModifiers();
    assertTrue(Modifier.isFinal(modifiers));
  }

  public void testCustomNullFail() {
    try {
      HsBiinvariantCoordinate.custom(RnManifold.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
