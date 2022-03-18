// code by jph
package ch.alpine.sophus.math.var;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.num.Pi;

public class LinearVariogramTest {
  @Test
  public void testSimple() {
    assertEquals(new LinearVariogram(Pi.VALUE).a(), Pi.VALUE);
  }
}
