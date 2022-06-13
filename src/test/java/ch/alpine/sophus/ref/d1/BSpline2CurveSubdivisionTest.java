// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
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

class BSpline2CurveSubdivisionTest {
  private static final CurveSubdivision CURVE_SUBDIVISION = new BSpline2CurveSubdivision(RnGroup.INSTANCE);

  @Test
  void testCyclic() {
    Tensor cyclic = CURVE_SUBDIVISION.cyclic(Tensors.vector(1, 2, 3, 4));
    assertEquals(cyclic, Tensors.fromString("{5/4, 7/4, 9/4, 11/4, 13/4, 15/4, 13/4, 7/4}"));
  }

  @Test
  void testSimple() {
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = CURVE_SUBDIVISION.cyclic(tensor);
    ExactTensorQ.require(actual);
    Tensor expected = Tensors.fromString("{{3/4, 1/4}, {1/4, 3/4}, {-1/4, 3/4}, {-3/4, 1/4}, {-3/4, -1/4}, {-1/4, -3/4}, {1/4, -3/4}, {3/4, -1/4}}");
    assertEquals(expected, actual);
  }

  @Test
  void testString() {
    Tensor string = CURVE_SUBDIVISION.string(Tensors.vector(10, 11.));
    assertEquals(string, Tensors.vector(10.25, 10.75));
    assertFalse(ExactTensorQ.of(string));
  }

  @Test
  void testStringTwo() {
    Tensor curve = Tensors.vector(0, 1);
    Tensor refined = CURVE_SUBDIVISION.string(curve);
    assertEquals(refined, Tensors.fromString("{1/4, 3/4}"));
    ExactTensorQ.require(refined);
  }

  @Test
  void testStringOne() {
    Tensor curve = Tensors.vector(1);
    Tensor refined = CURVE_SUBDIVISION.string(curve);
    assertEquals(refined, Tensors.fromString("{1}"));
    ExactTensorQ.require(refined);
  }

  @Test
  void testStringEmpty() {
    Tensor curve = Tensors.vector();
    Tensor refined = CURVE_SUBDIVISION.string(curve);
    assertTrue(Tensors.isEmpty(refined));
    ExactTensorQ.require(refined);
  }

  @Test
  void testCyclicEmpty() {
    Tensor curve = Tensors.vector();
    Tensor refined = CURVE_SUBDIVISION.cyclic(curve);
    assertTrue(Tensors.isEmpty(refined));
    ExactTensorQ.require(refined);
  }

  @Test
  void testStringRange() {
    int length = 9;
    Tensor curve = Range.of(0, length + 1);
    Tensor refined = CURVE_SUBDIVISION.string(curve);
    Tensor tensor = Subdivide.of(0, length, length * 2).map(scalar -> scalar.add(RationalScalar.of(1, 4)));
    assertEquals(refined, tensor.extract(0, tensor.length() - 1));
    ExactTensorQ.require(refined);
  }

  @Test
  void testSingleton() {
    Tensor singleton = Tensors.of(Tensors.vector(1, 2, 3));
    assertEquals(CURVE_SUBDIVISION.cyclic(singleton), singleton);
    assertEquals(CURVE_SUBDIVISION.string(singleton), singleton);
  }

  @Test
  void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator fps = new BSpline2CurveSubdivision(RnGroup.INSTANCE)::cyclic;
    TensorUnaryOperator copy = Serialization.copy(fps);
    assertEquals(copy.apply(CirclePoints.of(10)), fps.apply(CirclePoints.of(10)));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> new BSpline2CurveSubdivision(null));
  }
}
