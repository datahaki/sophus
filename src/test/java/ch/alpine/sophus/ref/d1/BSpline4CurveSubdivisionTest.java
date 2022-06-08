// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.clt.ClothoidBuilders;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
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

class BSpline4CurveSubdivisionTest {
  @Test
  public void testSplit2Lo() {
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split2lo(RnGroup.INSTANCE);
    ScalarUnaryOperator operator = Rationalize.withDenominatorLessEquals(100);
    Tensor tensor = CirclePoints.of(4).map(operator);
    Tensor actual = Nest.of(curveSubdivision::cyclic, tensor, 1);
    ExactTensorQ.require(actual);
    assertEquals(actual.extract(0, 3), Tensors.fromString("{{5/8, -1/4}, {5/8, 1/4}, {1/4, 5/8}}"));
  }

  @Test
  public void test2Lo() {
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split3(RnGroup.INSTANCE, RationalScalar.of(1, 6));
    Tensor tensor = curveSubdivision.string(UnitVector.of(5, 2));
    assertEquals(tensor, Tensors.fromString("{0, 1/16, 5/16, 5/8, 5/8, 5/16, 1/16, 0}"));
  }

  @Test
  public void testSplit3() {
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split3(RnGroup.INSTANCE);
    Tensor tensor = curveSubdivision.string(UnitVector.of(5, 2));
    assertEquals(tensor, Tensors.fromString("{0, 1/16, 5/16, 5/8, 5/8, 5/16, 1/16, 0}"));
  }

  @Test
  public void test2Hi() {
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split2hi(RnGroup.INSTANCE);
    Tensor tensor = curveSubdivision.string(UnitVector.of(5, 2));
    assertEquals(tensor, Tensors.fromString("{0, 1/16, 5/16, 5/8, 5/8, 5/16, 1/16, 0}"));
  }

  @Test
  public void testCyclic() {
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split2lo(RnGroup.INSTANCE);
    Tensor vector = Tensors.vector(0, 1, 2, 3);
    Tensor string = curveSubdivision.cyclic(vector);
    assertEquals(string, Tensors.fromString("{1, 1/2, 3/4, 5/4, 7/4, 9/4, 5/2, 2}"));
    ExactTensorQ.require(string);
  }

  @Test
  public void testString() {
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split2lo(RnGroup.INSTANCE);
    Tensor vector = Tensors.vector(0, 1, 2, 3);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Tensors.fromString("{1/4, 3/4, 5/4, 7/4, 9/4, 11/4}"));
    ExactTensorQ.require(string);
  }

  @Test
  public void testStringTwo() {
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split2lo(RnGroup.INSTANCE);
    Tensor vector = Tensors.vector(0, 1);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Tensors.fromString("{1/4, 3/4}"));
    ExactTensorQ.require(string);
  }

  @Test
  public void testStringOne() {
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split2lo(RnGroup.INSTANCE);
    Tensor vector = Tensors.vector(3);
    Tensor string = curveSubdivision.string(vector);
    assertEquals(string, Tensors.vector(3));
    ExactTensorQ.require(string);
  }

  @Test
  public void testEmpty() {
    Tensor curve = Tensors.vector();
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split2lo(RnGroup.INSTANCE);
    assertEquals(curveSubdivision.string(curve), Tensors.empty());
    assertEquals(curveSubdivision.cyclic(curve), Tensors.empty());
  }

  @Test
  public void testSingleton() {
    Tensor singleton = Tensors.of(Tensors.vector(1, 2, 3));
    CurveSubdivision curveSubdivision = BSpline4CurveSubdivision.split2lo(ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder());
    assertEquals(curveSubdivision.cyclic(singleton), singleton);
    assertEquals(curveSubdivision.string(singleton), singleton);
  }

  @Test
  public void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator fps = BSpline4CurveSubdivision.split2lo(RnGroup.INSTANCE)::cyclic;
    TensorUnaryOperator copy = Serialization.copy(fps);
    assertEquals(copy.apply(CirclePoints.of(10)), fps.apply(CirclePoints.of(10)));
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> BSpline4CurveSubdivision.split2lo(null));
    assertThrows(Exception.class, () -> BSpline4CurveSubdivision.split2hi(null));
    assertThrows(Exception.class, () -> BSpline4CurveSubdivision.split3(null));
    assertThrows(Exception.class, () -> BSpline4CurveSubdivision.split3(null, RealScalar.ONE));
  }
}
