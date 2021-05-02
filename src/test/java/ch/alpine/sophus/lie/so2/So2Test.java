// code by jph
package ch.alpine.sophus.lie.so2;

import java.io.IOException;

import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.num.Pi;
import junit.framework.TestCase;

public class So2Test extends TestCase {
  public void testSimple() {
    assertEquals(So2.MOD.apply(Pi.VALUE), Pi.VALUE.negate());
    assertEquals(So2.MOD.apply(Pi.VALUE.negate()), Pi.VALUE.negate());
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    Serialization.copy(So2.MOD);
  }
}
