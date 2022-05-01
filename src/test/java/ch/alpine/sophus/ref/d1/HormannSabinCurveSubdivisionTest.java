// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.num.Rationalize;
import ch.alpine.tensor.red.Nest;

class HormannSabinCurveSubdivisionTest {
  @Test
  public void testSimple() {
    CurveSubdivision curveSubdivision = HormannSabinCurveSubdivision.split3(RnGeodesic.INSTANCE);
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = Nest.of(curveSubdivision::cyclic, tensor, 1);
    ExactTensorQ.require(actual);
    Tensor p = tensor.get(3);
    Tensor q = tensor.get(0);
    Tensor r = tensor.get(1);
    Tensor weights = Tensors.fromString("{1/4-3/32, 3/4+6/32, -3/32}");
    assertEquals(weights.dot(Tensors.of(p, q, r)), actual.get(0));
    assertEquals(weights.dot(Tensors.of(r, q, p)), actual.get(1));
  }

  @Test
  public void testDefault() {
    CurveSubdivision cs0 = HormannSabinCurveSubdivision.of(RnGeodesic.INSTANCE);
    CurveSubdivision cs1 = HormannSabinCurveSubdivision.split3(RnGeodesic.INSTANCE);
    assertEquals(cs0.string(UnitVector.of(10, 5)), cs1.string(UnitVector.of(10, 5)));
  }

  @Test
  public void testSplit2Hi() {
    CurveSubdivision cs0 = HormannSabinCurveSubdivision.of(RnGeodesic.INSTANCE);
    CurveSubdivision cs1 = HormannSabinCurveSubdivision.split2(RnGeodesic.INSTANCE);
    assertEquals(cs0.string(UnitVector.of(10, 5)), cs1.string(UnitVector.of(10, 5)));
  }

  @Test
  public void testString() {
    CurveSubdivision curveSubdivision = HormannSabinCurveSubdivision.split3(RnGeodesic.INSTANCE);
    Tensor vector = Tensors.vector(0, 1, 2, 3);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Tensors.fromString("{1/4, 3/4, 5/4, 7/4, 9/4, 11/4}"));
    ExactTensorQ.require(string);
  }

  @Test
  public void testStringTwo() {
    CurveSubdivision curveSubdivision = HormannSabinCurveSubdivision.split3(RnGeodesic.INSTANCE);
    Tensor vector = Tensors.vector(0, 1);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Tensors.fromString("{1/4, 3/4}"));
    ExactTensorQ.require(string);
  }

  @Test
  public void testStringOne() {
    CurveSubdivision curveSubdivision = HormannSabinCurveSubdivision.split3(RnGeodesic.INSTANCE);
    Tensor vector = Tensors.vector(3);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Tensors.vector(3));
    ExactTensorQ.require(string);
  }

  @Test
  public void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator fps = HormannSabinCurveSubdivision.split3(RnGeodesic.INSTANCE)::cyclic;
    TensorUnaryOperator copy = Serialization.copy(fps);
    assertEquals(copy.apply(CirclePoints.of(10)), fps.apply(CirclePoints.of(10)));
  }
}
