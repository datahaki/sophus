// code by jph
package ch.alpine.sophus.hs.s;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;

class SnAngleTest {
  @Test
  void testSerializable() {
    Serialization.copy(new SnAngle(UnitVector.of(4, 2)));
  }

  @Test
  void testMemberQFail() {
    assertThrows(Exception.class, () -> new SnAngle(Tensors.vector(1, 1)));
  }
}
