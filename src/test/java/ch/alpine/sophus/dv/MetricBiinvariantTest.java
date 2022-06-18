// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;

class MetricBiinvariantTest {
  @Test
  void test() {
    assertThrows(Exception.class, () -> new MetricBiinvariant(RnGroup.INSTANCE).coordinate(null));
    assertThrows(Exception.class, () -> new MetricBiinvariant(RnGroup.INSTANCE).weighting(null));
  }
}
