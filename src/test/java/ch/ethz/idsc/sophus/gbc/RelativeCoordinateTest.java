// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import junit.framework.TestCase;

public class RelativeCoordinateTest extends TestCase {
  public void testMod() {
    int modifiers = RelativeCoordinate.class.getModifiers();
    assertTrue(Modifier.isFinal(modifiers));
  }

  public void testCustomNullFail() {
    try {
      RelativeCoordinate.custom(RnManifold.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
