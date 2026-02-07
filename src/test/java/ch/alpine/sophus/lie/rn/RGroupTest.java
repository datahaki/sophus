// code by jph
package ch.alpine.sophus.lie.rn;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;

class RGroupTest {
  @Test
  void test() throws ClassNotFoundException, IOException {
    Serialization.copy(RGroup.INSTANCE);
  }
}
