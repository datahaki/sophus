// code by jph
package ch.ethz.idsc.sophus.ply.d2;

import ch.ethz.idsc.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class HilbertPolygonTest extends TestCase {
  public void testZeroClosedFail() {
    AssertFail.of(() -> HilbertPolygon.of(0));
  }
}
