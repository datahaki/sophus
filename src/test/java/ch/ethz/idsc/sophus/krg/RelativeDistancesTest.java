// code by jph
package ch.ethz.idsc.sophus.krg;

import java.lang.reflect.Modifier;

import junit.framework.TestCase;

public class RelativeDistancesTest extends TestCase {
  public void testVisibility() {
    int modifiers = RelativeDistances.class.getModifiers();
    assertFalse(Modifier.isPublic(modifiers));
  }
}
