// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Chop;

class PolygonAreaTest {
  @Test
  void testAreaTriangle() {
    Tensor poly = Tensors.fromString("{{1, 1}, {2, 1}, {1, 2}}");
    Scalar area = PolygonArea.of(poly);
    assertEquals(area, RationalScalar.HALF);
    ExactScalarQ.require(area);
  }

  @Test
  void testAreaCube() {
    Tensor poly = Tensors.fromString("{{1, 1}, {2, 1}, {2, 2}, {1, 2}}");
    Scalar area = PolygonArea.of(poly);
    assertEquals(area, RealScalar.ONE);
    ExactScalarQ.require(area);
  }

  @Test
  void testAreaEmpty() {
    Scalar area = PolygonArea.of(Tensors.empty());
    assertEquals(area, RealScalar.ZERO);
  }

  @Test
  void testAreaLine() {
    Tensor poly = Tensors.fromString("{{1, 1}, {2, 1}}");
    Scalar area = PolygonArea.of(poly);
    assertEquals(area, RealScalar.ZERO);
    ExactScalarQ.require(area);
  }

  @Test
  void testAreaPoint() {
    Tensor poly = Tensors.fromString("{{1, 1}}");
    Scalar area = PolygonArea.of(poly);
    assertEquals(area, RealScalar.ZERO);
    ExactScalarQ.require(area);
  }

  @Test
  void testAreaTriangleUnit() {
    Tensor poly = Tensors.fromString("{{1[m], 1[m]}, {2[m], 1[m]}, {1[m], 2[m]}}");
    Scalar area = PolygonArea.of(poly);
    assertEquals(area, Scalars.fromString("1/2[m^2]"));
    ExactScalarQ.require(area);
  }

  @Test
  void testAreaCubeUnit() {
    Tensor poly = Tensors.fromString("{{1[cm], 1[cm]}, {2[cm], 1[cm]}}");
    Scalar area = PolygonArea.of(poly);
    assertEquals(area, Scalars.fromString("0[cm^2]"));
    ExactScalarQ.require(area);
  }

  @Test
  void testAreaCirclePoints() {
    Scalar area = PolygonArea.of(CirclePoints.of(100));
    Chop._02.requireClose(area, Pi.VALUE);
  }

  @Test
  void testAreaCirclePointsReverse() {
    Scalar area = PolygonArea.of(Reverse.of(CirclePoints.of(100)));
    Chop._02.requireClose(area, Pi.VALUE.negate());
  }

  @Test
  void testArea1Unit() {
    Tensor poly = Tensors.fromString("{{1[m], 1[m]}}");
    Scalar area = PolygonArea.of(poly);
    assertEquals(area, Scalars.fromString("0[m^2]"));
    ExactScalarQ.require(area);
  }

  @Test
  void testArea2Unit() {
    Tensor poly = Tensors.fromString("{{1[m], 1[m]}, {2[m], 1[m]}}");
    Scalar area = PolygonArea.of(poly);
    assertEquals(area, Scalars.fromString("0[m^2]"));
    ExactScalarQ.require(area);
  }

  @Test
  void testFailScalar() {
    assertThrows(Exception.class, () -> PolygonArea.of(RealScalar.ONE));
  }

  @Test
  void testFailMatrix() {
    assertThrows(Exception.class, () -> PolygonArea.of(HilbertMatrix.of(3)));
  }
}
