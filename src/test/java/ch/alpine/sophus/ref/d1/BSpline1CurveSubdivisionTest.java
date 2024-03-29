// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.clt.ClothoidBuilder;
import ch.alpine.sophus.crv.clt.ClothoidBuilders;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.num.Rationalize;

class BSpline1CurveSubdivisionTest {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  @Test
  void testCyclic() {
    CurveSubdivision curveSubdivision = new BSpline1CurveSubdivision(RnGroup.INSTANCE);
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = curveSubdivision.cyclic(tensor);
    ExactTensorQ.require(actual);
    Tensor expected = Tensors.fromString("{{1, 0}, {1/2, 1/2}, {0, 1}, {-1/2, 1/2}, {-1, 0}, {-1/2, -1/2}, {0, -1}, {1/2, -1/2}}");
    assertEquals(expected, actual);
  }

  @Test
  void testString() {
    CurveSubdivision curveSubdivision = new BSpline1CurveSubdivision(RnGroup.INSTANCE);
    Tensor string = curveSubdivision.string(Tensors.fromString("{{0, 10}, {1, 12}}"));
    assertEquals(string, Tensors.fromString("{{0, 10}, {1/2, 11}, {1, 12}}"));
    ExactTensorQ.require(string);
  }

  @Test
  void testStringTwo() {
    Tensor curve = Tensors.vector(0, 1);
    CurveSubdivision curveSubdivision = new BSpline1CurveSubdivision(RnGroup.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{0, 1/2, 1}"));
    ExactTensorQ.require(refined);
  }

  @Test
  void testStringRange() {
    int length = 9;
    Tensor curve = Range.of(0, length + 1);
    CurveSubdivision curveSubdivision = new BSpline1CurveSubdivision(RnGroup.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Subdivide.of(0, length, length * 2));
    ExactTensorQ.require(refined);
  }

  @Test
  void testStringOne() {
    Tensor curve = Tensors.vector(8);
    CurveSubdivision curveSubdivision = new BSpline1CurveSubdivision(RnGroup.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertEquals(refined, Tensors.fromString("{8}"));
    ExactTensorQ.require(refined);
  }

  @Test
  void testStringEmpty() {
    Tensor curve = Tensors.vector();
    CurveSubdivision curveSubdivision = new BSpline1CurveSubdivision(RnGroup.INSTANCE);
    Tensor refined = curveSubdivision.string(curve);
    assertTrue(Tensors.isEmpty(refined));
    ExactTensorQ.require(refined);
  }

  @Test
  void testCyclicEmpty() {
    Tensor curve = Tensors.vector();
    CurveSubdivision curveSubdivision = new BSpline1CurveSubdivision(RnGroup.INSTANCE);
    Tensor refined = curveSubdivision.cyclic(curve);
    assertTrue(Tensors.isEmpty(refined));
    ExactTensorQ.require(refined);
  }

  @Test
  void testCirclePoints() {
    CurveSubdivision curveSubdivision = new BSpline1CurveSubdivision(RnGroup.INSTANCE);
    for (int n = 3; n < 10; ++n) {
      Tensor tensor = curveSubdivision.cyclic(CirclePoints.of(n));
      Tensor filter = Tensor.of(IntStream.range(0, tensor.length() / 2) //
          .map(i -> i * 2) //
          .mapToObj(tensor::get));
      assertEquals(filter, CirclePoints.of(n));
    }
  }

  @Test
  void testSingleton() {
    Tensor singleton = Tensors.of(Tensors.vector(1, 2, 3));
    CurveSubdivision curveSubdivision = new BSpline1CurveSubdivision(CLOTHOID_BUILDER);
    assertEquals(curveSubdivision.cyclic(singleton), singleton);
    assertEquals(curveSubdivision.string(singleton), singleton);
  }

  @Test
  void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator fps = new BSpline1CurveSubdivision(RnGroup.INSTANCE)::cyclic;
    TensorUnaryOperator copy = Serialization.copy(fps);
    assertEquals(copy.apply(CirclePoints.of(10)), fps.apply(CirclePoints.of(10)));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> new BSpline1CurveSubdivision(null));
  }
}
