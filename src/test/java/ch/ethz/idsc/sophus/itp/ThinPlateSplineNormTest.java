// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.tensor.RealScalar;
import junit.framework.TestCase;

public class ThinPlateSplineNormTest extends TestCase {
  public void testFailNonPositive() {
    try {
      ThinPlateSplineNorm.of(RealScalar.ZERO);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
