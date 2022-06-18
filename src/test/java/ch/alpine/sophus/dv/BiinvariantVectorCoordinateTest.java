// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class BiinvariantVectorCoordinateTest {
  @Test
  void testNonPublic() {
    assertFalse(Modifier.isPublic(BiinvariantVectorCoordinate.class.getModifiers()));
  }
}
