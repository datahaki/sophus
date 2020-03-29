// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGeodesic;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class HsGeodesicTest extends TestCase {
  public void testSe2() {
    HsGeodesic lieGroupGeodesic = new HsGeodesic(Se2CoveringManifold.HS_EXP);
    Tensor p = Tensors.vector(1, 2, 3);
    Tensor q = Tensors.vector(4, 5, 6);
    Scalar lambda = RealScalar.of(0.7);
    Tensor tensor = lieGroupGeodesic.split(p, q, lambda);
    Tensor split = Se2CoveringGeodesic.INSTANCE.split(p, q, lambda);
    assertEquals(tensor, split);
  }

  public void testNullFail() {
    try {
      new HsGeodesic(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
