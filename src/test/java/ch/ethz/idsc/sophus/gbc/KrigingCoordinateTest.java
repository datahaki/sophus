// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class KrigingCoordinateTest extends TestCase {
  public void testNull1Fail() {
    AssertFail.of(() -> KrigingCoordinate.of(t -> t, RnManifold.INSTANCE, null));
  }

  public void testNull2Fail() {
    AssertFail.of(() -> KrigingCoordinate.of(null, RnManifold.INSTANCE, IdentityMatrix.of(4)));
  }

  public void testNull3Fail() {
    AssertFail.of(() -> KrigingCoordinate.of(t -> t, null, IdentityMatrix.of(4)));
  }
}
