// code by jph
package ch.alpine.sophus.hs.hn;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;

class HnAngleTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    HnAngle hnAngle = Serialization.copy(new HnAngle(UnitVector.of(4, 3)));
    assertTrue(Scalars.isZero(hnAngle.apply(UnitVector.of(4, 3))));
    assertThrows(Exception.class, () -> hnAngle.log(UnitVector.of(4, 2)));
    assertThrows(Exception.class, () -> hnAngle.apply(UnitVector.of(4, 2)));
  }

  @Test
  public void testMemberFail() {
    assertThrows(Exception.class, () -> new HnAngle(UnitVector.of(4, 2)));
  }
}
