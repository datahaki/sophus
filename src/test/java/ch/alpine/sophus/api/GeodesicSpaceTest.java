// code by jph
package ch.alpine.sophus.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2c.Se2CoveringGeodesic;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;

class GeodesicSpaceTest {
  @Test
  public void testSe2() {
    GeodesicSpace lieGroupGeodesic = Se2CoveringManifold.INSTANCE;
    Tensor p = Tensors.vector(1, 2, 3);
    Tensor q = Tensors.vector(4, 5, 6);
    Scalar lambda = RealScalar.of(0.7);
    Tensor tensor = lieGroupGeodesic.split(p, q, lambda);
    Tensor split = Se2CoveringGeodesic.INSTANCE.split(p, q, lambda);
    assertEquals(tensor, split);
  }

  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    GeodesicSpace hsMidpoint = Serialization.copy(RnManifold.INSTANCE);
    Tensor tensor = hsMidpoint.midpoint(Tensors.vector(2, 0, 8), Tensors.vector(4, 2, 10));
    ExactTensorQ.require(tensor);
    assertEquals(tensor, Tensors.vector(3, 1, 9));
  }
}
