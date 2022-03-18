// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.lie.r2.ConvexHull;
import ch.alpine.tensor.sca.Sign;

public class CogPointsTest {
  @Test
  public void testSimple() {
    Tensor polygon = CogPoints.of(10, RealScalar.of(10.2), RealScalar.of(3.2));
    assertEquals(Dimensions.of(polygon), Arrays.asList(40, 2));
    Sign.requirePositive(PolygonArea.of(polygon));
    Tensor convex = ConvexHull.of(polygon);
    assertEquals(Dimensions.of(convex), Arrays.asList(20, 2));
  }

  @Test
  public void testToggled() {
    Tensor tensor = CogPoints.of(10, RealScalar.of(10.2), RealScalar.of(30.2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(40, 2));
    Tensor convex = ConvexHull.of(tensor);
    assertEquals(Dimensions.of(convex), Arrays.asList(20, 2));
  }
}
