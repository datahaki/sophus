// code by jph
package ch.alpine.sophus.gbc;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;

public class MetricCoordinateTest {
  @Test
  public void testCustomNullFail() {
    AssertFail.of(() -> new MetricCoordinate(null));
  }
}
