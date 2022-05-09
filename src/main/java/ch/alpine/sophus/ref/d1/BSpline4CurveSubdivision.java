// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.crv.clt.Clothoid;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

/** quartic B-spline
 * 
 * To encode the quartic B-spline mask, there are exactly two geodesic averages
 * that consist of two splits, but infinitely many geodesic averages that consist
 * of three splits. */
public enum BSpline4CurveSubdivision {
  ;
  private static final Scalar P2_3 = RationalScalar.of(2, 3);
  private static final Scalar P1_16 = RationalScalar.of(1, 16);

  /** geodesic split suggested by Dyn/Sharon 2014 p.16 who show that the scheme
   * with this split has a contractivity factor of mu = 5/6. "The contractivity
   * guarantees the convergence of these schemes from all initial data."
   * 
   * <p>experiments with random data in SE(2) show that this split performs best
   * among the three geodesic splits listed here, and therefore Dyn/Sharon is
   * the preferred scheme to use on homogeneous spaces.
   * 
   * Careful: do not use on {@link Clothoid}
   * 
   * Reference:
   * "Manifold-valued subdivision schemes based on geodesic inductive averaging"
   * by Dyn, Sharon, 2014
   * 
   * @param geodesicSpace
   * @return */
  public static CurveSubdivision split2lo(GeodesicSpace geodesicSpace) {
    return Split2LoDual3PointCurveSubdivision.of(geodesicSpace, P2_3, P1_16);
  }

  // ---
  private static final Scalar P11_16 = RationalScalar.of(11, 16);
  private static final Scalar P1_11 = RationalScalar.of(1, 11);

  /** @param geodesicSpace
   * @return */
  public static CurveSubdivision split2hi(GeodesicSpace geodesicSpace) {
    return Split2HiDual3PointCurveSubdivision.of(geodesicSpace, P11_16, P1_11);
  }

  // ---
  private static final Scalar P5 = RealScalar.of(5);
  private static final Scalar P16 = RealScalar.of(16);

  /** geodesic split suggested by Hakenberg 2018
   * most suitable for {@link Clothoid}
   * 
   * @param geodesicSpace
   * @return */
  public static CurveSubdivision split3(GeodesicSpace geodesicSpace) {
    return split3(geodesicSpace, RationalScalar.HALF);
  }

  /** function generalizes all variants above with {1/16, 1/2, 11/16}
   * 
   * @param geodesicSpace
   * @param value in the interval [1/16, 11/16] give the best results
   * @return */
  public static CurveSubdivision split3(GeodesicSpace geodesicSpace, Scalar value) {
    return Split3Dual3PointCurveSubdivision.of(geodesicSpace, //
        P5.divide(P16.multiply(value.subtract(RealScalar.ONE))).add(RealScalar.ONE), //
        value.multiply(P16).reciprocal(), value);
  }
}
