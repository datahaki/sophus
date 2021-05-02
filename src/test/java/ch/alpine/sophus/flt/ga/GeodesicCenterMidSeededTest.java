// code by jph
package ch.alpine.sophus.flt.ga;

import ch.alpine.sophus.flt.ga.GeodesicCenterMidSeeded.Splits;
import ch.alpine.sophus.math.win.UniformWindowSampler;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.sca.win.GaussianWindow;
import junit.framework.TestCase;

public class GeodesicCenterMidSeededTest extends TestCase {
  public void testSplitsEvenFail() {
    Splits splits = new GeodesicCenterMidSeeded.Splits(UniformWindowSampler.of(GaussianWindow.FUNCTION));
    splits.apply(5);
    AssertFail.of(() -> splits.apply(4));
  }

  public void testSplitsNullFail() {
    AssertFail.of(() -> new GeodesicCenterMidSeeded.Splits(null));
  }
}
