// code by jph
package ch.ethz.idsc.sophus.hs;

import java.lang.reflect.Modifier;

import junit.framework.TestCase;

public class HsInverseDistanceCoordinateTest extends TestCase {
  public void testMod() {
    int modifiers = HsInverseDistanceCoordinate.class.getModifiers();
    assertTrue(Modifier.isFinal(modifiers));
  }
}
