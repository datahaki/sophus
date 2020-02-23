// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.tensor.RealScalar;
import junit.framework.TestCase;

public class HeTargetTest extends TestCase {
  public void testFailNegative() {
    try {
      new HeTarget(RnNorm.INSTANCE, RealScalar.of(-1));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
