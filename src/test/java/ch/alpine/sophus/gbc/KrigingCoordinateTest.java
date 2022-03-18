// code by jph
package ch.alpine.sophus.gbc;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.mat.IdentityMatrix;

public class KrigingCoordinateTest {
  @Test
  public void testNull1Fail() {
    AssertFail.of(() -> KrigingCoordinate.of(t -> t, RnManifold.INSTANCE, null));
  }

  @Test
  public void testNull2Fail() {
    AssertFail.of(() -> KrigingCoordinate.of(null, RnManifold.INSTANCE, IdentityMatrix.of(4)));
  }

  @Test
  public void testNull3Fail() {
    AssertFail.of(() -> KrigingCoordinate.of(t -> t, null, IdentityMatrix.of(4)));
  }
}
