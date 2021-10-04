// code by jph
package ch.alpine.sophus.crv.d2;

import junit.framework.TestCase;

public class FranklinPnpolyTest extends TestCase {
  public void testVisibility() {
    assertEquals(FranklinPnpoly.class.getModifiers() & 1, 0);
  }
}
