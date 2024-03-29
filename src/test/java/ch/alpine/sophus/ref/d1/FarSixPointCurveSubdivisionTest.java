// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

class FarSixPointCurveSubdivisionTest {
  @Test
  void testP1() {
    FarSixPointCurveSubdivision spcs = new FarSixPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ONE, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(3, 256));
  }

  @Test
  void testP2() {
    FarSixPointCurveSubdivision spcs = new FarSixPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ONE, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(-25, 256));
  }

  @Test
  void testP3() {
    FarSixPointCurveSubdivision spcs = new FarSixPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(150, 256));
  }

  @Test
  void testP4() {
    FarSixPointCurveSubdivision spcs = new FarSixPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE, RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(150, 256));
  }

  @Test
  void testP5() {
    FarSixPointCurveSubdivision spcs = new FarSixPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE, RealScalar.ZERO);
    assertEquals(p1, RationalScalar.of(-25, 256));
  }

  @Test
  void testP6() {
    FarSixPointCurveSubdivision spcs = new FarSixPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor p1 = spcs.center(RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE);
    assertEquals(p1, RationalScalar.of(3, 256));
  }

  @Test
  void testString5() {
    FarSixPointCurveSubdivision spcs = new FarSixPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor string = spcs.string(Tensors.vector(1, 0, 0, 0, 0));
    Scalar scalar = string.Get(4);
    assertTrue(Scalars.isZero(scalar));
    assertEquals(string.length(), 5 + 4);
  }

  @Test
  void testString6() {
    FarSixPointCurveSubdivision spcs = new FarSixPointCurveSubdivision(RnGroup.INSTANCE);
    Tensor string = spcs.string(Tensors.vector(1, 0, 0, 0, 0, 0));
    Scalar scalar = string.Get(5);
    assertTrue(Scalars.nonZero(scalar));
    assertEquals(string.length(), 6 + 5);
  }
}
