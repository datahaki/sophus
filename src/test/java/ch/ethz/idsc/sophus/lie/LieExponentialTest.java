// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.son.SonGroup;
import junit.framework.TestCase;

public class LieExponentialTest extends TestCase {
  public void testNullFail() {
    try {
      LieExponential.of(SonGroup.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      LieExponential.of(null, Se2CoveringExponential.INSTANCE);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
