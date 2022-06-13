// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Sign;

class StarPointsTest {
  @Test
  void testSimple() {
    Tensor polygon = StarPoints.of(4, RealScalar.ONE, RealScalar.of(0.3));
    assertEquals(polygon.length(), 8);
    Sign.requirePositive(PolygonArea.of(polygon));
  }
}
