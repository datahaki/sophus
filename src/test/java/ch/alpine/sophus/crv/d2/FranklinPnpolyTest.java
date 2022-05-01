// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FranklinPnpolyTest {
  @Test
  public void testVisibility() {
    assertEquals(FranklinPnpoly.class.getModifiers() & 1, 0);
  }
}
