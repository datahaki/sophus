// code by jph
package ch.alpine.sophus.flt.ga;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.img.MeanFilter;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Unitize;
import junit.framework.TestCase;

public class GeodesicMeanFilterTest extends TestCase {
  public void testSimple() {
    for (int radius = 0; radius < 4; ++radius) {
      TensorUnaryOperator tensorUnaryOperator = GeodesicMeanFilter.of(RnGeodesic.INSTANCE, radius);
      Tensor tensor = Tensors.vector(1, 2, 3, 4, 6, 7);
      Tensor result = tensorUnaryOperator.apply(tensor);
      assertEquals(result.length(), tensor.length());
    }
  }

  public void testRadiusOne() {
    TensorUnaryOperator tensorUnaryOperator = GeodesicMeanFilter.of(RnGeodesic.INSTANCE, 1);
    Tensor tensor = UnitVector.of(10, 5);
    Tensor result = tensorUnaryOperator.apply(tensor);
    assertEquals(Total.of(result), RealScalar.ONE);
    Tensor expect = UnitVector.of(10, 4).add(UnitVector.of(10, 5)).add(UnitVector.of(10, 6));
    assertEquals(Unitize.of(result), expect);
  }

  public void testMultiRadius() {
    for (int radius = 0; radius < 5; ++radius) {
      TensorUnaryOperator tensorUnaryOperator = GeodesicMeanFilter.of(RnGeodesic.INSTANCE, radius);
      Tensor tensor = UnitVector.of(2 * radius + 1, radius);
      Tensor result = tensorUnaryOperator.apply(tensor);
      Tensor expect = MeanFilter.of(tensor, radius);
      assertEquals(result.Get(radius), expect.Get(radius));
      ExactTensorQ.require(result);
    }
  }

  public void testBiUnits() {
    int radius = 2;
    TensorUnaryOperator tensorUnaryOperator = GeodesicMeanFilter.of(RnGeodesic.INSTANCE, radius);
    Tensor tensor = Tensors.vector(0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0);
    Tensor result = tensorUnaryOperator.apply(tensor);
    Tensor expect = MeanFilter.of(tensor, radius);
    assertEquals(result, expect);
    ExactTensorQ.require(result);
  }
}