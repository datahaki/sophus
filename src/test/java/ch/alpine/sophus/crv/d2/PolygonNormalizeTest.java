// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

public class PolygonNormalizeTest {
  @Test
  public void testSimple() {
    Tensor polygon = Tensors.fromString("{{0, 0}, {2, 0}, {2, 2}, {0, 2}}");
    Tensor tensor = PolygonNormalize.of(polygon, RealScalar.ONE);
    assertEquals(tensor.toString(), "{{-1/2, -1/2}, {1/2, -1/2}, {1/2, 1/2}, {-1/2, 1/2}}");
  }
}
