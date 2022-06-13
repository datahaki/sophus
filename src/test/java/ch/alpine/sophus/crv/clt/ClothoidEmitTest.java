// code by jph
package ch.alpine.sophus.crv.clt;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;

class ClothoidEmitTest {
  @Test
  void testAngles() throws ClassNotFoundException, IOException {
    Serialization.copy(ClothoidBuilders.SE2_COVERING);
  }
}
