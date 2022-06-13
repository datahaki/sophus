// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2BiinvariantMeans;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class CurveSubdivisionTest {
  private static void _checkSym(CurveSubdivision cs, Tensor tensor) {
    Tensor forward = cs.string(tensor);
    Tensor reverse = cs.string(Reverse.of(tensor));
    Chop._12.requireClose(Reverse.of(forward), reverse);
  }

  @Test
  void testSymmetric() {
    Distribution distribution = UniformDistribution.of(-2, 3);
    for (int length = 0; length < 10; ++length) {
      Tensor tensor = RandomVariate.of(distribution, length, 2);
      _checkSym(new BSpline1CurveSubdivision(RnGroup.INSTANCE), tensor);
      _checkSym(new BSpline2CurveSubdivision(RnGroup.INSTANCE), tensor);
      _checkSym(new BSpline3CurveSubdivision(RnGroup.INSTANCE), tensor);
      _checkSym(BSpline4CurveSubdivision.split2lo(RnGroup.INSTANCE), tensor);
      _checkSym(new FourPointCurveSubdivision(RnGroup.INSTANCE), tensor);
      _checkSym(DodgsonSabinCurveSubdivision.INSTANCE, tensor);
      _checkSym(HormannSabinCurveSubdivision.split3(RnGroup.INSTANCE), tensor);
    }
  }

  public static final CurveSubdivision[] CURVE_SUBDIVISIONS = { //
      new BSpline1CurveSubdivision(Se2Group.INSTANCE), //
      new BSpline2CurveSubdivision(Se2Group.INSTANCE), //
      new BSpline3CurveSubdivision(Se2Group.INSTANCE), //
      BSpline4CurveSubdivision.split2lo(Se2Group.INSTANCE), //
      BSpline4CurveSubdivision.split2hi(Se2Group.INSTANCE), //
      BSpline4CurveSubdivision.split3(Se2Group.INSTANCE), //
      new BSpline5CurveSubdivision(Se2Group.INSTANCE), //
      BSpline6CurveSubdivision.of(Se2Group.INSTANCE), //
      new FourPointCurveSubdivision(Se2Group.INSTANCE), //
      DodgsonSabinCurveSubdivision.INSTANCE, //
      DualC2FourPointCurveSubdivision.cubic(Se2Group.INSTANCE), //
      DualC2FourPointCurveSubdivision.tightest(Se2Group.INSTANCE), //
      new SixPointCurveSubdivision(Se2Group.INSTANCE), //
      new EightPointCurveSubdivision(Se2Group.INSTANCE), //
      new MSpline3CurveSubdivision(Se2BiinvariantMeans.GLOBAL), //
      // LaneRiesenfeldCurveSubdivision.of(H2Midpoint.INSTANCE, 1), //
      // LaneRiesenfeldCurveSubdivision.of(H2Midpoint.INSTANCE, 2), //
      LaneRiesenfeld3CurveSubdivision.of(Se2Group.INSTANCE), //
  };

  @Test
  void testNullFail() {
    for (CurveSubdivision curveSubdivision : CURVE_SUBDIVISIONS) {
      assertThrows(Exception.class, () -> curveSubdivision.string(null));
      assertThrows(Exception.class, () -> curveSubdivision.cyclic(null));
    }
  }

  @Test
  void testScalarFail() {
    for (CurveSubdivision curveSubdivision : CURVE_SUBDIVISIONS) {
      assertThrows(Exception.class, () -> curveSubdivision.string(Pi.HALF));
      assertThrows(Exception.class, () -> curveSubdivision.cyclic(Pi.VALUE));
    }
  }
}
