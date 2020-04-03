// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.sophus.math.id.InverseDistanceWeighting;
import junit.framework.TestCase;

public class HsInverseDistanceCoordinateTest extends TestCase {
  public void testMod() {
    int modifiers = HsInverseDistanceCoordinate.class.getModifiers();
    assertTrue(Modifier.isFinal(modifiers));
  }

  public void testNullWeightingFail() {
    try {
      HsInverseDistanceCoordinate.custom(SnManifold.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNullManifoldFail() {
    try {
      HsInverseDistanceCoordinate.custom(null, InverseDistanceWeighting.of(RnMetric.INSTANCE));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
