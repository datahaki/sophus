// code by jph
package ch.ethz.idsc.sophus.dv;

import java.lang.reflect.Modifier;

import junit.framework.TestCase;

public class MetricDistanceVectorTest extends TestCase {
  public void testVisibility() {
    int modifiers = MetricDistanceVector.class.getModifiers();
    assertTrue(Modifier.isPublic(modifiers));
  }
}