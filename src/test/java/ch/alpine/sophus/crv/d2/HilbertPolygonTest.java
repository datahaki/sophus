// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class HilbertPolygonTest {
  @Test
  public void testZeroClosedFail() {
    assertThrows(Exception.class, () -> HilbertPolygon.of(0));
  }
}
