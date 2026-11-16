// code by jph
package ch.alpine.sophus.clt;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;

class ClothoidEmitTest {
  @Test
  void testAngles() {
    Serialization.copy(ClothoidBuilders.SE2_COVERING);
  }
}
