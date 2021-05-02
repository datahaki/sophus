// code by jph
package ch.alpine.sophus.ref.d1h;

import java.io.IOException;

import ch.alpine.sophus.lie.LieTransport;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class Hermite1SubdivisionTest extends TestCase {
  public void testQuantity() throws ClassNotFoundException, IOException {
    TestHelper.checkQuantity(Hermite1Subdivisions.standard(RnManifold.INSTANCE, LieTransport.INSTANCE));
  }

  public void testNullFail() {
    AssertFail.of(() -> Hermite1Subdivisions.standard(Se2CoveringManifold.INSTANCE, null));
    AssertFail.of(() -> Hermite1Subdivisions.standard(null, LieTransport.INSTANCE));
  }
}
