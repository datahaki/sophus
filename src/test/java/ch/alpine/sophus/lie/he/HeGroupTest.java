// code by jph
package ch.alpine.sophus.lie.he;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;

public class HeGroupTest {
  @Test
  public void testSimple() {
    AssertFail.of(() -> HeGroup.INSTANCE.element(null));
  }
}
