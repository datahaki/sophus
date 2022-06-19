// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.mat.IdentityMatrix;

class KrigingCoordinateTest {
  @Test
  void testNull1Fail() {
    assertThrows(Exception.class, () -> new KrigingCoordinate(new HsDesign(RnGroup.INSTANCE), t -> t, null));
  }

  @Test
  void testNull2Fail() {
    assertThrows(Exception.class, () -> new KrigingCoordinate(new HsDesign(RnGroup.INSTANCE), null, IdentityMatrix.of(4)));
  }

  @Test
  void testNull3Fail() {
    assertThrows(Exception.class, () -> new KrigingCoordinate(null, t -> t, IdentityMatrix.of(4)));
  }
}
