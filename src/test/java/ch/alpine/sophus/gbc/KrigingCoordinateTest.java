// code by jph
package ch.alpine.sophus.gbc;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.mat.IdentityMatrix;

class KrigingCoordinateTest {
  @Test
  void testNull1Fail() {
    assertThrows(Exception.class, () -> KrigingCoordinate.of(t -> t, RnGroup.INSTANCE, null));
  }

  @Test
  void testNull2Fail() {
    assertThrows(Exception.class, () -> KrigingCoordinate.of(null, RnGroup.INSTANCE, IdentityMatrix.of(4)));
  }

  @Test
  void testNull3Fail() {
    assertThrows(Exception.class, () -> KrigingCoordinate.of(t -> t, null, IdentityMatrix.of(4)));
  }
}
