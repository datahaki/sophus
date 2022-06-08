// code by jph
package ch.alpine.sophus.hs.rpn;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;

class RpnMetricTest {
  @Test
  public void testSimple() {
    Tolerance.CHOP.requireZero(RpnMetric.INSTANCE.distance(Tensors.vector(2, 0, 0), Tensors.vector(+10, 0, 0)));
    Tolerance.CHOP.requireZero(RpnMetric.INSTANCE.distance(Tensors.vector(2, 0, 0), Tensors.vector(-10, 0, 0)));
  }
}
