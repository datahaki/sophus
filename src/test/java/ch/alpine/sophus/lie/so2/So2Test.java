// code by jph
package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.num.Pi;

class So2Test {
  @Test
  void testSimple() {
    assertEquals(So2.MOD.apply(Pi.VALUE), Pi.VALUE.negate());
    assertEquals(So2.MOD.apply(Pi.VALUE.negate()), Pi.VALUE.negate());
  }

  @Test
  void testSerializable() throws ClassNotFoundException, IOException {
    Serialization.copy(So2.MOD);
  }
}
