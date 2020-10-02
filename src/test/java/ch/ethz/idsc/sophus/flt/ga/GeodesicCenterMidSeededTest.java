// code by jph
package ch.ethz.idsc.sophus.flt.ga;

import ch.ethz.idsc.sophus.flt.ga.GeodesicCenterMidSeeded.Splits;
import ch.ethz.idsc.sophus.math.win.UniformWindowSampler;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.sca.win.GaussianWindow;
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
