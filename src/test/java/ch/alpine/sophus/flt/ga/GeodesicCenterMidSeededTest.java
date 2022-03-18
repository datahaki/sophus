// code by jph
package ch.alpine.sophus.flt.ga;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.flt.ga.GeodesicCenterMidSeeded.Splits;
import ch.alpine.sophus.math.win.UniformWindowSampler;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.sca.win.GaussianWindow;

public class GeodesicCenterMidSeededTest {
  @Test
  public void testSplitsEvenFail() {
    Splits splits = new GeodesicCenterMidSeeded.Splits(UniformWindowSampler.of(GaussianWindow.FUNCTION));
    splits.apply(5);
    AssertFail.of(() -> splits.apply(4));
  }

  @Test
  public void testSplitsNullFail() {
    AssertFail.of(() -> new GeodesicCenterMidSeeded.Splits(null));
  }
}
