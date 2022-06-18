// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MetricCoordinateTest {
  @Test
  void testCustomNullFail() {
    assertThrows(Exception.class, () -> new MetricCoordinate(null));
  }
}
