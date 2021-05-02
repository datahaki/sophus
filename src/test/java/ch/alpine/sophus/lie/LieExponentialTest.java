// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.so.SoGroup;
import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class LieExponentialTest extends TestCase {
  public void testNullFail() {
    AssertFail.of(() -> LieExponential.of(SoGroup.INSTANCE, null));
    AssertFail.of(() -> LieExponential.of(null, Se2CoveringExponential.INSTANCE));
  }
}
