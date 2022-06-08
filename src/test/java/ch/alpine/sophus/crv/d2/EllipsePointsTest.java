// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;

class EllipsePointsTest {
  @Test
  public void testScaled() {
    int n = 11;
    Tensor tensor = EllipsePoints.of(n, RealScalar.of(2), RealScalar.of(0.5));
    assertEquals(Dimensions.of(tensor), Arrays.asList(n, 2));
  }
}
