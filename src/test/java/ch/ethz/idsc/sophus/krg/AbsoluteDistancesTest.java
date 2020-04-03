// code by jph
package ch.ethz.idsc.sophus.krg;

import java.lang.reflect.Modifier;

import junit.framework.TestCase;

public class AbsoluteDistancesTest extends TestCase {
  public void testVisibility() {
    int modifiers = AbsoluteDistances.class.getModifiers();
    assertFalse(Modifier.isPublic(modifiers));
  }
}
