// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.lang.reflect.Modifier;

import junit.framework.TestCase;

public class Relative1CoordinateTest extends TestCase {
  public void testMod() {
    int modifiers = Relative1Coordinate.class.getModifiers();
    assertTrue(Modifier.isFinal(modifiers));
  }
}
