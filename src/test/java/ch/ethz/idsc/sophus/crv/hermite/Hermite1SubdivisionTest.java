// code by jph
package ch.ethz.idsc.sophus.crv.hermite;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.sophus.lie.rn.RnGroup;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import junit.framework.TestCase;

public class Hermite1SubdivisionTest extends TestCase {
  public void testQuantity() throws ClassNotFoundException, IOException {
    TestHelper.checkQuantity(Hermite1Subdivisions.standard(RnGroup.INSTANCE, RnExponential.INSTANCE));
  }

  public void testNullFail() {
    try {
      Hermite1Subdivisions.standard(Se2CoveringGroup.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Hermite1Subdivisions.standard(null, Se2CoveringExponential.INSTANCE);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
