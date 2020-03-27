// code by jph
package ch.ethz.idsc.sophus.hs;

import java.lang.reflect.Modifier;

import junit.framework.TestCase;

public class HsBiinvariantCoordinateTest extends TestCase {
  public void testMod() {
    int modifiers = HsBiinvariantCoordinate.class.getModifiers();
    assertTrue(Modifier.isFinal(modifiers));
  }
}
