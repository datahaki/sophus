// code by jph
package ch.alpine.sophus.hs.st;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StRandomSampleTest {
  @Test
  void testFail() {
    assertThrows(Exception.class, () -> StRandomSample.of(3, -1));
    assertThrows(Exception.class, () -> StRandomSample.of(3, 4));
    assertThrows(Exception.class, () -> StRandomSample.of(-3, -4));
  }
}
