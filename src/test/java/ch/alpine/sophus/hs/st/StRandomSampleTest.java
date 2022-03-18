// code by jph
package ch.alpine.sophus.hs.st;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;

public class StRandomSampleTest {
  @Test
  public void testFail() {
    AssertFail.of(() -> StRandomSample.of(3, -1));
    AssertFail.of(() -> StRandomSample.of(3, 4));
    AssertFail.of(() -> StRandomSample.of(-3, -4));
  }
}
