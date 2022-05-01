// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.h2.H2Midpoint;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.lie.se2.Se2BiinvariantMeans;
import ch.alpine.sophus.lie.se2.Se2Geodesic;
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
  public void testSymmetric() {
    Distribution distribution = UniformDistribution.of(-2, 3);
    for (int length = 0; length < 10; ++length) {
      Tensor tensor = RandomVariate.of(distribution, length, 2);
      _checkSym(new BSpline1CurveSubdivision(RnGeodesic.INSTANCE), tensor);
      _checkSym(new BSpline2CurveSubdivision(RnGeodesic.INSTANCE), tensor);
      _checkSym(new BSpline3CurveSubdivision(RnGeodesic.INSTANCE), tensor);
      _checkSym(BSpline4CurveSubdivision.split2lo(RnGeodesic.INSTANCE), tensor);
      _checkSym(new FourPointCurveSubdivision(RnGeodesic.INSTANCE), tensor);
      _checkSym(DodgsonSabinCurveSubdivision.INSTANCE, tensor);
      _checkSym(HormannSabinCurveSubdivision.split3(RnGeodesic.INSTANCE), tensor);
    }
  }

  public static final CurveSubdivision[] CURVE_SUBDIVISIONS = { //
      new BSpline1CurveSubdivision(Se2Geodesic.INSTANCE), //
      new BSpline2CurveSubdivision(Se2Geodesic.INSTANCE), //
      new BSpline3CurveSubdivision(Se2Geodesic.INSTANCE), //
      BSpline4CurveSubdivision.split2lo(Se2Geodesic.INSTANCE), //
      BSpline4CurveSubdivision.split2hi(Se2Geodesic.INSTANCE), //
      BSpline4CurveSubdivision.split3(Se2Geodesic.INSTANCE), //
      new BSpline5CurveSubdivision(Se2Geodesic.INSTANCE), //
      BSpline6CurveSubdivision.of(Se2Geodesic.INSTANCE), //
      new FourPointCurveSubdivision(Se2Geodesic.INSTANCE), //
      DodgsonSabinCurveSubdivision.INSTANCE, //
      DualC2FourPointCurveSubdivision.cubic(Se2Geodesic.INSTANCE), //
      DualC2FourPointCurveSubdivision.tightest(Se2Geodesic.INSTANCE), //
      new SixPointCurveSubdivision(Se2Geodesic.INSTANCE), //
      new EightPointCurveSubdivision(Se2Geodesic.INSTANCE), //
      new MSpline3CurveSubdivision(Se2BiinvariantMeans.GLOBAL), //
      LaneRiesenfeldCurveSubdivision.of(H2Midpoint.INSTANCE, 1), //
      LaneRiesenfeldCurveSubdivision.of(H2Midpoint.INSTANCE, 2), //
      LaneRiesenfeld3CurveSubdivision.of(Se2Geodesic.INSTANCE), //
  };

  @Test
  public void testNullFail() {
    for (CurveSubdivision curveSubdivision : CURVE_SUBDIVISIONS) {
      assertThrows(Exception.class, () -> curveSubdivision.string(null));
      assertThrows(Exception.class, () -> curveSubdivision.cyclic(null));
    }
  }

  @Test
  public void testScalarFail() {
    for (CurveSubdivision curveSubdivision : CURVE_SUBDIVISIONS) {
      assertThrows(Exception.class, () -> curveSubdivision.string(Pi.HALF));
      assertThrows(Exception.class, () -> curveSubdivision.cyclic(Pi.VALUE));
    }
  }
}
