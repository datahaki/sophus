// code by jph
package ch.alpine.sophus.ref.d1h;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;

class Hermite2SubdivisionTest {
  @Test
  void testQuantity() throws ClassNotFoundException, IOException {
    TestHelper.checkQuantity(Hermite2Subdivisions.standard(RnGroup.INSTANCE));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> Hermite2Subdivisions.standard(null));
  }
}
