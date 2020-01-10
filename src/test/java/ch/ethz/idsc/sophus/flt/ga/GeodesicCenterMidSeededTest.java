// code by jph
package ch.ethz.idsc.sophus.flt.ga;

import ch.ethz.idsc.sophus.flt.ga.GeodesicCenterMidSeeded.Splits;
import ch.ethz.idsc.sophus.math.win.UniformWindowSampler;
import ch.ethz.idsc.tensor.sca.win.GaussianWindow;
import junit.framework.TestCase;

public class GeodesicCenterMidSeededTest extends TestCase {
  public void testSplitsEvenFail() {
    Splits splits = new GeodesicCenterMidSeeded.Splits(UniformWindowSampler.of(GaussianWindow.FUNCTION));
    splits.apply(5);
    try {
      splits.apply(4);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testSplitsNullFail() {
    try {
      new GeodesicCenterMidSeeded.Splits(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
