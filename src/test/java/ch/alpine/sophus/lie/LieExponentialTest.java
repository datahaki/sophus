// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LieExponentialTest {
  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> LieExponential.of(null));
  }
}
