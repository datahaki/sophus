// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Chop;

class Se2CoveringMetricTest {
  @Test
  public void testPlanar() {
    Scalar scalar = Se2CoveringMetric.INSTANCE.distance(Tensors.vector(1, 1, 0), Tensors.vector(4, 5, 0));
    assertEquals(scalar, RealScalar.of(5));
  }

  @Test
  public void testTurn() {
    Scalar scalar = Se2CoveringMetric.INSTANCE.distance(Tensors.vector(1, 2, 3), Tensors.vector(1, 2, 7));
    Chop._14.requireClose(scalar, RealScalar.of(4));
  }

  @Test
  public void testMiddle() {
    Tensor p = Tensors.vector(1, .7, 2);
    Tensor q = Tensors.vector(2, .3, 3.3);
    Scalar dq = Se2CoveringMetric.INSTANCE.distance(p, q);
    Tensor m = Se2CoveringGroup.INSTANCE.split(p, q, RationalScalar.HALF);
    Scalar dm = Se2CoveringMetric.INSTANCE.distance(p, m);
    Chop._14.requireClose(dq.divide(dm), RealScalar.of(2));
  }
}
