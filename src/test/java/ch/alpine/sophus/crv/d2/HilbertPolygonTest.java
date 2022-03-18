// code by jph
package ch.alpine.sophus.crv.d2;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;

public class HilbertPolygonTest {
  @Test
  public void testZeroClosedFail() {
    AssertFail.of(() -> HilbertPolygon.of(0));
  }
}
