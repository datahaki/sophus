// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class MetricBiinvariantTest {
  @Test
  void testNonPublic() {
    assertFalse(Modifier.isPublic(MetricBiinvariant.class.getModifiers()));
  }
}
