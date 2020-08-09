// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.lie.so3.So3Manifold;
import junit.framework.TestCase;

public class SubdivideTransportTest extends TestCase {
  public void testSubdivideFail() {
    try {
      SubdivideTransport.of(PoleLadder.of(So3Manifold.INSTANCE), new HsGeodesic(So3Manifold.INSTANCE), 0);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNullFail() {
    try {
      SubdivideTransport.of(null, new HsGeodesic(So3Manifold.INSTANCE), 2);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      SubdivideTransport.of(PoleLadder.of(So3Manifold.INSTANCE), null, 2);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
