// code by ob
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.win.GaussianWindow;

class GeodesicFIRnFilterTest {
  @Test
  public void testTranslation() {
    Tensor p = Tensors.vector(0, 0, 0);
    Tensor q = Tensors.vector(1, 1, 0);
    Tensor r = q.add(q);
    Tensor s = r.add(q);
    Tensor control = Tensors.of(p, q, r, s);
    GeodesicSpace geodesicSpace = Se2Group.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(geodesicSpace, GaussianWindow.FUNCTION);
    Tensor refined = GeodesicFIRnFilter.of(tensorUnaryOperator, geodesicSpace, 2, RealScalar.of(Math.random())).apply(control);
    assertEquals(refined.get(0), p);
    assertEquals(refined.get(1), q);
    assertEquals(refined.get(3), Tensors.vector(3.0, 3.0, 0.0));
  }

  @Test
  public void testRotation() {
    Tensor p = Tensors.vector(0, 0, 0);
    Tensor q = Tensors.vector(0, 0, 1);
    Tensor r = q.add(q);
    Tensor s = r.add(q);
    Tensor control = Tensors.of(p, q, r, s);
    GeodesicSpace geodesicSpace = Se2Group.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(geodesicSpace, GaussianWindow.FUNCTION);
    Tensor refined = GeodesicFIRnFilter.of(tensorUnaryOperator, geodesicSpace, 2, RealScalar.of(Math.random())).apply(control);
    assertEquals(refined.get(0), p);
    assertEquals(refined.get(1), q);
    assertEquals(refined.get(3), Tensors.vector(0.0, 0.0, 3.0));
  }

  @Test
  public void testCombined() {
    Tensor p = Tensors.vector(0, 0, 0);
    Tensor q = Tensors.vector(1, 1, 1);
    Tensor r = q.add(q);
    Tensor s = r.add(q);
    Tensor control = Tensors.of(p, q, r, s);
    GeodesicSpace geodesicSpace = Se2Group.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(geodesicSpace, GaussianWindow.FUNCTION);
    Tensor refined = GeodesicFIRnFilter.of(tensorUnaryOperator, geodesicSpace, 2, RealScalar.of(0.5)).apply(control);
    assertEquals(refined.get(0), p);
    assertEquals(refined.get(1), q);
    Chop._12.requireClose(refined.get(3), Tensors.vector(2.3494156605301217, 3.190886645338018, 3.0));
  }

  @Test
  public void testLinear() {
    Tensor control = Tensors.fromString("{{0, 0, 0}, {1, 0, 0}, {2, 0, 0}, {3, 0, 0}, {4, 0, 0}, {5, 0, 0}}");
    Scalar alpha = RealScalar.of(0.5);
    final int radius = 3;
    GeodesicSpace geodesicSpace = Se2Group.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(geodesicSpace, GaussianWindow.FUNCTION);
    Tensor actual = GeodesicFIRnFilter.of(tensorUnaryOperator, geodesicSpace, radius, alpha).apply(control);
    assertEquals(control, actual);
  }

  @Test
  public void testOnlyMeasurement() {
    Tensor control = Tensors.fromString("{{0, 0.2, 0}, {1, 0, 0}, {2, 7, 0}, {3, 9, 0}, {3, 0, 0}, {-1, 0, -1}}");
    Scalar alpha = RealScalar.of(1);
    final int radius = 3;
    GeodesicSpace geodesicSpace = Se2Group.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(geodesicSpace, GaussianWindow.FUNCTION);
    Tensor actual = GeodesicFIRnFilter.of(tensorUnaryOperator, geodesicSpace, radius, alpha).apply(control);
    Chop._09.requireClose(control, actual);
  }

  @Test
  public void testOnlyPrediction() {
    Tensor control = Tensors.fromString("{{0, 0.2, 0}, {1, 0, 0}, {2, 7, 0}, {3, 9, 0}, {3, 0, 0}, {-1, 0, -1}}");
    Scalar alpha = RealScalar.of(0);
    final int radius = 2;
    GeodesicSpace geodesicSpace = Se2Group.INSTANCE;
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(geodesicSpace, GaussianWindow.FUNCTION);
    Tensor actual = GeodesicFIRnFilter.of(tensorUnaryOperator, geodesicSpace, radius, alpha).apply(control);
    Tensor expected = Tensors.fromString("{{0, 0.2, 0}, {1, 0, 0}, {2.0, -0.2, 0.0}, {3.0, 14.0, 0.0}, {4.0, 11.0, 0.0}, {3.0, -9.0, 0.0}}");
    Chop._09.requireClose(expected, actual);
  }
}
