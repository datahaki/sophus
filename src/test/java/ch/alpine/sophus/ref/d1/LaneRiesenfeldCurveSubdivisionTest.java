// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.clt.ClothoidBuilder;
import ch.alpine.sophus.crv.clt.ClothoidBuilders;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.chq.ExactTensorQ;

class LaneRiesenfeldCurveSubdivisionTest {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  @Test
  void testDeg1() {
    CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, 1);
    Tensor string = curveSubdivision.string(Tensors.vector(1, 2, 3, 4));
    assertEquals(string, Tensors.fromString("{1, 3/2, 2, 5/2, 3, 7/2, 4}"));
    ExactTensorQ.require(string);
  }

  @Test
  void testDeg2() {
    CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, 2);
    Tensor string = curveSubdivision.string(Tensors.vector(1, 2, 3, 4));
    assertEquals(string, Tensors.fromString("{5/4, 7/4, 9/4, 11/4, 13/4, 15/4}"));
    ExactTensorQ.require(string);
  }

  @Test
  void testDeg3() {
    CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, 3);
    Tensor string = curveSubdivision.string(Tensors.vector(1, 2, 3, 4));
    assertEquals(string, Tensors.fromString("{1, 3/2, 2, 5/2, 3, 7/2, 4}"));
    ExactTensorQ.require(string);
  }

  @Test
  void testDeg4() {
    CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, 4);
    Tensor string = curveSubdivision.string(Tensors.vector(1, 2, 3, 4));
    assertEquals(string, Tensors.fromString("{5/4, 7/4, 9/4, 11/4, 13/4, 15/4}"));
    ExactTensorQ.require(string);
  }

  @Test
  void testDeg5() {
    CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, 5);
    Tensor string = curveSubdivision.string(Tensors.vector(1, 2, 3, 4));
    assertEquals(string, Tensors.fromString("{1, 3/2, 2, 5/2, 3, 7/2, 4}"));
    ExactTensorQ.require(string);
  }

  @Test
  void testCyc2() {
    CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, 2);
    Tensor cyclic = curveSubdivision.cyclic(Tensors.vector(1, 2, 3, 4));
    assertEquals(cyclic, Tensors.fromString("{5/4, 7/4, 9/4, 11/4, 13/4, 15/4, 13/4, 7/4}"));
    ExactTensorQ.require(cyclic);
  }

  @Test
  void testCyc3() {
    CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, 3);
    CurveSubdivision curveSubdivisiom = new BSpline3CurveSubdivision(RnGroup.INSTANCE);
    Tensor tensor = Tensors.vector(1, 2, 3, 4);
    assertEquals(curveSubdivisiom.cyclic(tensor), Tensors.fromString("{3/2, 3/2, 2, 5/2, 3, 7/2, 7/2, 5/2}"));
    Tensor cyclic = curveSubdivision.cyclic(tensor);
    assertEquals(curveSubdivisiom.cyclic(tensor), cyclic);
    ExactTensorQ.require(cyclic);
  }

  @Test
  void testEmpty() {
    for (int degree = 1; degree < 4; ++degree) {
      CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(CLOTHOID_BUILDER, degree);
      assertEquals(curveSubdivision.cyclic(Tensors.empty()), Tensors.empty());
      assertEquals(curveSubdivision.string(Tensors.empty()), Tensors.empty());
    }
  }

  @Test
  void testSingleton() {
    Tensor singleton = Tensors.of(Tensors.vector(1, 2, 3));
    for (int degree = 1; degree < 4; ++degree) {
      CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(CLOTHOID_BUILDER, degree);
      assertEquals(curveSubdivision.cyclic(singleton), singleton);
      assertEquals(curveSubdivision.string(singleton), singleton);
    }
  }

  @Test
  void testDegMatrix() {
    for (int degree = 1; degree < 6; ++degree) {
      CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, degree);
      for (int length = 0; length < 10; ++length) {
        Tensor tensor = Range.of(0, length);
        curveSubdivision.string(tensor);
        curveSubdivision.cyclic(tensor);
      }
    }
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> LaneRiesenfeldCurveSubdivision.of(null, 3));
  }

  @Test
  void testDegreeFail() {
    assertThrows(Exception.class, () -> LaneRiesenfeldCurveSubdivision.of(RnGroup.INSTANCE, 0));
  }
}
