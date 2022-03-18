// code by jph
package ch.alpine.sophus.lie;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.so.SoGroup;
import ch.alpine.sophus.usr.AssertFail;

public class LieExponentialTest {
  @Test
  public void testNullFail() {
    AssertFail.of(() -> LieExponential.of(SoGroup.INSTANCE, null));
    AssertFail.of(() -> LieExponential.of(null, Se2CoveringExponential.INSTANCE));
  }
}
