// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.lang.reflect.Modifier;

import junit.framework.TestCase;

public class ObsoleteCoordinateTest extends TestCase {
  public void testMod() {
    int modifiers = ObsoleteCoordinate.class.getModifiers();
    assertTrue(Modifier.isFinal(modifiers));
  }
}
