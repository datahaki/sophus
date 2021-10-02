// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class HilbertPolygonTest extends TestCase {
  public void testZeroClosedFail() {
    AssertFail.of(() -> HilbertPolygon.of(0));
  }
}
