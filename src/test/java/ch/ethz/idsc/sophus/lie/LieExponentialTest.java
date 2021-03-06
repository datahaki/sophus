// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.so.SoGroup;
import ch.ethz.idsc.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class LieExponentialTest extends TestCase {
  public void testNullFail() {
    AssertFail.of(() -> LieExponential.of(SoGroup.INSTANCE, null));
    AssertFail.of(() -> LieExponential.of(null, Se2CoveringExponential.INSTANCE));
  }
}
