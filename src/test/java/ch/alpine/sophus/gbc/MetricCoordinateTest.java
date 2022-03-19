// code by jph
package ch.alpine.sophus.gbc;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MetricCoordinateTest {
  @Test
  public void testCustomNullFail() {
    assertThrows(Exception.class, () -> new MetricCoordinate(null));
  }
}
