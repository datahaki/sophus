// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.so.SoGroup;

class LieExponentialTest {
  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> LieExponential.of(SoGroup.INSTANCE, null));
    assertThrows(Exception.class, () -> LieExponential.of(null, Se2CoveringExponential.INSTANCE));
  }
}
