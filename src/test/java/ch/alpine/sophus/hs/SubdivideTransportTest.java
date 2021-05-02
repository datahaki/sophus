// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.lie.so3.So3Manifold;
import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class SubdivideTransportTest extends TestCase {
  public void testSubdivideFail() {
    AssertFail.of(() -> SubdivideTransport.of( //
        PoleLadder.of(So3Manifold.INSTANCE), new HsGeodesic(So3Manifold.INSTANCE), 0));
  }

  public void testNullFail() {
    AssertFail.of(() -> SubdivideTransport.of(null, new HsGeodesic(So3Manifold.INSTANCE), 2));
    AssertFail.of(() -> SubdivideTransport.of(PoleLadder.of(So3Manifold.INSTANCE), null, 2));
  }
}
