// code by jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.red.Mean;

class GeodesicMeanTest {
  @Test
  public void testSimple() {
    for (int radius = 0; radius <= 5; ++radius) {
      TensorUnaryOperator geodesicMean = GeodesicMean.of(RnGeodesic.INSTANCE);
      Tensor input = Range.of(0, 2 * radius + 1);
      Tensor apply = geodesicMean.apply(input);
      assertEquals(apply, Mean.of(input));
    }
  }

  @Test
  public void testMultiRadius() {
    for (int radius = 0; radius < 5; ++radius) {
      TensorUnaryOperator geodesicMean = GeodesicMean.of(RnGeodesic.INSTANCE);
      Tensor input = UnitVector.of(2 * radius + 1, radius);
      Tensor apply = geodesicMean.apply(input);
      assertEquals(apply, Mean.of(input));
    }
  }
}
