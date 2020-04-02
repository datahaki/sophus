// code by jph
package ch.ethz.idsc.sophus.krg;

import java.lang.reflect.Modifier;

import junit.framework.TestCase;

public class PseudoDistancesTest extends TestCase {
  public void testVisibility() {
    int modifiers = PseudoDistances.class.getModifiers();
    assertTrue(Modifier.isPublic(modifiers));
  }
}
