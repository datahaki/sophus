// code by jph
package ch.alpine.sophus.hs;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so3.So3Manifold;
import ch.alpine.sophus.usr.AssertFail;

public class SubdivideTransportTest {
  @Test
  public void testSubdivideFail() {
    AssertFail.of(() -> SubdivideTransport.of( //
        new PoleLadder(So3Manifold.INSTANCE), new HsGeodesic(So3Manifold.INSTANCE), 0));
  }

  @Test
  public void testNullFail() {
    AssertFail.of(() -> SubdivideTransport.of(null, new HsGeodesic(So3Manifold.INSTANCE), 2));
    AssertFail.of(() -> SubdivideTransport.of(new PoleLadder(So3Manifold.INSTANCE), null, 2));
  }
}
