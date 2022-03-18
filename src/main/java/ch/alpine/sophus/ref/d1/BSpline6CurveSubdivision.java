// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.api.Geodesic;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;

/** cubic B-spline */
public enum BSpline6CurveSubdivision {
  ;
  private static final Scalar _5_6 = RationalScalar.of(5, 6);
  private static final Scalar _1_22 = RationalScalar.of(1, 22);
  private static final Scalar _11_32 = RationalScalar.of(11, 32);

  public static CurveSubdivision of(Geodesic geodesicInterface) {
    return new Dual4PointCurveSubdivision(geodesicInterface, _5_6, _1_22, _11_32);
  }
}
