// code by jph
package ch.alpine.sophus.clt;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;

class ClothoidEmitTest {
  @Test
  public void testAngles() throws ClassNotFoundException, IOException {
    Serialization.copy(ClothoidBuilders.SE2_COVERING);
  }
}
