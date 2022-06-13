// code by jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.flt.ga.GeodesicCenterMidSeeded.Splits;
import ch.alpine.sophus.math.win.UniformWindowSampler;
import ch.alpine.tensor.sca.win.GaussianWindow;

class GeodesicCenterMidSeededTest {
  @Test
  void testSplitsEvenFail() {
    Splits splits = new GeodesicCenterMidSeeded.Splits(UniformWindowSampler.of(GaussianWindow.FUNCTION));
    splits.apply(5);
    assertThrows(Exception.class, () -> splits.apply(4));
  }

  @Test
  void testSplitsNullFail() {
    assertThrows(Exception.class, () -> new GeodesicCenterMidSeeded.Splits(null));
  }
}
