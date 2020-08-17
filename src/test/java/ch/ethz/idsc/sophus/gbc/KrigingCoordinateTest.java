// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class KrigingCoordinateTest extends TestCase {
  public void testNull1Fail() {
    try {
      KrigingCoordinate.of(t -> t, RnManifold.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNull2Fail() {
    try {
      KrigingCoordinate.of(null, RnManifold.INSTANCE, IdentityMatrix.of(4));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNull3Fail() {
    try {
      KrigingCoordinate.of(t -> t, null, IdentityMatrix.of(4));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
