// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.Pi;
import junit.framework.TestCase;

public class ClothoidImplTest extends TestCase {
  public void testSimple() {
    try {
      new ClothoidImpl(null, LagrangeQuadratic.interp(Pi.HALF, Pi.TWO, Pi.VALUE), Tensors.vector(1, 2));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
