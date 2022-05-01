// code by jph
package ch.alpine.sophus.ref.d1h;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieTransport;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;

class Hermite2SubdivisionTest {
  @Test
  public void testQuantity() throws ClassNotFoundException, IOException {
    TestHelper.checkQuantity(Hermite2Subdivisions.standard(RnManifold.INSTANCE, LieTransport.INSTANCE));
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> Hermite2Subdivisions.standard(Se2CoveringManifold.INSTANCE, null));
    assertThrows(Exception.class, () -> Hermite2Subdivisions.standard(null, LieTransport.INSTANCE));
  }
}
