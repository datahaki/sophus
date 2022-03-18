// code by jph
package ch.alpine.sophus.gbc.d2;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;

public class ThreePointWeightingTest {
  @Test
  public void testSimple() {
    AssertFail.of(() -> new ThreePointWeighting(null));
  }
}
