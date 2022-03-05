// code by jph
package ch.alpine.sophus.math.var;

import ch.alpine.tensor.num.Pi;
import junit.framework.TestCase;

public class LinearVariogramTest extends TestCase {
  public void testSimple() {
    assertEquals(new LinearVariogram(Pi.VALUE).a(), Pi.VALUE);
  }
}
