// code by jph
package ch.alpine.sophus.lie.he;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class HeGroupTest {
  @Test
  public void testSimple() {
    assertThrows(Exception.class, () -> HeGroup.INSTANCE.element(null));
  }
}
