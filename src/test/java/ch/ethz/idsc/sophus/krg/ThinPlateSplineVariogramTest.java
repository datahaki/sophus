// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.tensor.RealScalar;
import junit.framework.TestCase;

public class ThinPlateSplineVariogramTest extends TestCase {
  public void testFailNonPositive() {
    try {
      ThinPlateSplineVariogram.of(RealScalar.ZERO);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
