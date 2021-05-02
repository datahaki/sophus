// code by jph
package ch.alpine.sophus.ref.d1;

import java.io.IOException;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.num.Rationalize;
import junit.framework.TestCase;

public class BSpline2CurveSubdivisionTest extends TestCase {
  private static final CurveSubdivision CURVE_SUBDIVISION = new BSpline2CurveSubdivision(RnGeodesic.INSTANCE);

  public void testCyclic() {
    Tensor cyclic = CURVE_SUBDIVISION.cyclic(Tensors.vector(1, 2, 3, 4));
    assertEquals(cyclic, Tensors.fromString("{5/4, 7/4, 9/4, 11/4, 13/4, 15/4, 13/4, 7/4}"));
  }

  public void testSimple() {
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = CURVE_SUBDIVISION.cyclic(tensor);
    ExactTensorQ.require(actual);
    Tensor expected = Tensors.fromString("{{3/4, 1/4}, {1/4, 3/4}, {-1/4, 3/4}, {-3/4, 1/4}, {-3/4, -1/4}, {-1/4, -3/4}, {1/4, -3/4}, {3/4, -1/4}}");
    assertEquals(expected, actual);
  }

  public void testString() {
    Tensor string = CURVE_SUBDIVISION.string(Tensors.vector(10, 11.));
    assertEquals(string, Tensors.vector(10.25, 10.75));
    assertFalse(ExactTensorQ.of(string));
  }

  public void testStringTwo() {
    Tensor curve = Tensors.vector(0, 1);
    Tensor refined = CURVE_SUBDIVISION.string(curve);
    assertEquals(refined, Tensors.fromString("{1/4, 3/4}"));
    ExactTensorQ.require(refined);
  }

  public void testStringOne() {
    Tensor curve = Tensors.vector(1);
    Tensor refined = CURVE_SUBDIVISION.string(curve);
    assertEquals(refined, Tensors.fromString("{1}"));
    ExactTensorQ.require(refined);
  }

  public void testStringEmpty() {
    Tensor curve = Tensors.vector();
    Tensor refined = CURVE_SUBDIVISION.string(curve);
    assertTrue(Tensors.isEmpty(refined));
    ExactTensorQ.require(refined);
  }

  public void testCyclicEmpty() {
    Tensor curve = Tensors.vector();
    Tensor refined = CURVE_SUBDIVISION.cyclic(curve);
    assertTrue(Tensors.isEmpty(refined));
    ExactTensorQ.require(refined);
  }

  public void testStringRange() {
    int length = 9;
    Tensor curve = Range.of(0, length + 1);
    Tensor refined = CURVE_SUBDIVISION.string(curve);
    Tensor tensor = Subdivide.of(0, length, length * 2).map(scalar -> scalar.add(RationalScalar.of(1, 4)));
    assertEquals(refined, tensor.extract(0, tensor.length() - 1));
    ExactTensorQ.require(refined);
  }

  public void testSingleton() {
    Tensor singleton = Tensors.of(Tensors.vector(1, 2, 3));
    assertEquals(CURVE_SUBDIVISION.cyclic(singleton), singleton);
    assertEquals(CURVE_SUBDIVISION.string(singleton), singleton);
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator fps = new BSpline2CurveSubdivision(RnGeodesic.INSTANCE)::cyclic;
    TensorUnaryOperator copy = Serialization.copy(fps);
    assertEquals(copy.apply(CirclePoints.of(10)), fps.apply(CirclePoints.of(10)));
  }

  public void testNullFail() {
    AssertFail.of(() -> new BSpline2CurveSubdivision(null));
  }
}
