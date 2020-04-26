// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.krg.ShepardWeighting;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
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
    WeightingInterface weightingInterface = ShepardWeighting.absolute(RnManifold.INSTANCE, 1);
    try {
      HsInverseDistanceCoordinate.custom(null, weightingInterface);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
