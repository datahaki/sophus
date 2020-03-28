// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class So2HelperTest extends TestCase {
  public void testSimple() {
    StaticHelper.rangeQ(Tensors.vector(1, 2, 3));
    try {
      StaticHelper.rangeQ(Tensors.vector(1, 2, 7));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
