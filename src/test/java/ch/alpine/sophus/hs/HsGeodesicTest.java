// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.lie.se2c.Se2CoveringGeodesic;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class HsGeodesicTest extends TestCase {
  public void testSe2() {
    HsGeodesic lieGroupGeodesic = new HsGeodesic(Se2CoveringManifold.INSTANCE);
    Tensor p = Tensors.vector(1, 2, 3);
    Tensor q = Tensors.vector(4, 5, 6);
    Scalar lambda = RealScalar.of(0.7);
    Tensor tensor = lieGroupGeodesic.split(p, q, lambda);
    Tensor split = Se2CoveringGeodesic.INSTANCE.split(p, q, lambda);
    assertEquals(tensor, split);
  }

  public void testNullFail() {
    AssertFail.of(() -> new HsGeodesic(null));
  }
}
