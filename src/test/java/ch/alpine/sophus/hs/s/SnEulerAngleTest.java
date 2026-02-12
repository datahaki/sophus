// code by jph
package ch.alpine.sophus.hs.s;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.CauchyDistribution;

class SnEulerAngleTest {
  @Test
  void testSimple() {
    for (int d = 0; d < 6; ++d) {
      Tensor angles = RandomVariate.of(CauchyDistribution.standard(), d);
      Tensor tensor = SnEulerAngle.of(angles);
      SnManifold.INSTANCE.isPointQ().require(tensor);
    }
  }
}
