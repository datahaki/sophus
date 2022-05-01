// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;

public class SixPointCurveSubdivisionTest {
  @Test
  public void testP1() {
    SixPointCurveSubdivision spcs = new SixPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ONE, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(3, 256));
  }

  @Test
  public void testP2() {
    SixPointCurveSubdivision spcs = new SixPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ONE, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(-25, 256));
  }

  @Test
  public void testP3() {
    SixPointCurveSubdivision spcs = new SixPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(150, 256));
  }

  @Test
  public void testP4() {
    SixPointCurveSubdivision spcs = new SixPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE, RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(150, 256));
  }

  @Test
  public void testP5() {
    SixPointCurveSubdivision spcs = new SixPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(-25, 256));
  }

  @Test
  public void testP6() {
    SixPointCurveSubdivision spcs = new SixPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE);
    assertEquals(p1, RationalScalar.of(3, 256));
  }

  @Test
  public void testString5() {
    SixPointCurveSubdivision spcs = new SixPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor string = spcs.string(Tensors.vector(1, 0, 0, 0, 0));
    Scalar scalar = string.Get(4);
    assertTrue(Scalars.isZero(scalar));
    assertEquals(string.length(), 5 + 4);
  }

  @Test
  public void testString6() {
    SixPointCurveSubdivision spcs = new SixPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor string = spcs.string(Tensors.vector(1, 0, 0, 0, 0, 0));
    Scalar scalar = string.Get(5);
    assertTrue(Scalars.nonZero(scalar));
    assertEquals(string.length(), 6 + 5);
  }

  @Test
  public void testCyclic() {
    SixPointCurveSubdivision spcs = new SixPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor cyclic = spcs.cyclic(Tensors.vector(1, 0, 0, 0, 0, 0));
    Tensor expected = Tensors.fromString("{1, 75/128, 0, -25/256, 0, 3/256, 0, 3/256, 0, -25/256, 0, 75/128}");
    assertEquals(cyclic, expected);
    ExactTensorQ.require(cyclic);
  }
}
