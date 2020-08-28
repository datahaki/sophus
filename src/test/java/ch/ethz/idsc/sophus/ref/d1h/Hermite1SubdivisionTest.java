// code by jph
package ch.ethz.idsc.sophus.ref.d1h;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnTransport;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import junit.framework.TestCase;

public class Hermite1SubdivisionTest extends TestCase {
  public void testQuantity() throws ClassNotFoundException, IOException {
    TestHelper.checkQuantity(Hermite1Subdivisions.standard(RnManifold.HS_EXP, RnTransport.INSTANCE));
  }

  public void testNullFail() {
    try {
      Hermite1Subdivisions.standard(Se2CoveringManifold.HS_EXP, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Hermite1Subdivisions.standard(null, RnTransport.INSTANCE);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
