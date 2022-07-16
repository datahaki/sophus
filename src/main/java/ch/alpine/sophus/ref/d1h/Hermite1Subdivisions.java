// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

/** Merrien interpolatory Hermite subdivision scheme of order two
 * implementation for R^n
 * 
 * This scheme reproduces polynomials of degree 1. Moreover it reproduces
 * polynomials of degree 2 if and only if lambda == -1/8, and
 * polynomials of degree 3 if also mu == -1/2.
 * 
 * References:
 * "A family of Hermite interpolants by bisection algorithms"
 * by Merrien, 1992 */
public enum Hermite1Subdivisions {
  ;
  private static final Scalar _1_4 = RationalScalar.of(1, 4);

  /** References:
   * "Scalar and Hermite subdivision schemes"
   * by Dubuc, 2006, p. 391, H1[lambda, mu]
   * Theorem:
   * H1[lambda, mu] is C1 iff 0 < -lambda < 1/2, 0 < mu < min(-1/(2lambda), 3/(1 + 2lambda))
   * 
   * "de Rham Transform of a Hermite Subdivision Scheme"
   * by Dubuc, Merrien, 2007, p. 9, H1[lambda, mu]
   * 
   * "From Hermite to Stationary Subdivision Schemes in One and Several Variables"
   * by Merrien, Sauer, 2010, p. 26, H1[lambda, mu]
   * 
   * "Dual Hermite subdivision schemes of de Rham-type"
   * by Conti, Merrien, Romani, 2015, p. 11, H1[lambda, mu]
   * 
   * @param homogeneousSpace
   * @param lambda
   * @param mu
   * @return */
  public static HermiteSubdivision of(HomogeneousSpace homogeneousSpace, HermiteLoConfig hermiteLoParam) {
    return new Hermite1Subdivision( //
        homogeneousSpace, //
        hermiteLoParam.lambda(), //
        RealScalar.ONE.subtract(hermiteLoParam.mu()).multiply(RationalScalar.HALF), //
        hermiteLoParam.mu().multiply(_1_4));
  }

  /** lambda == -1/8
   * mu == -1/2
   * 
   * Reference:
   * "Construction of Hermite subdivision schemes reproducing polynomials"
   * by Byeongseon Jeong, Jungho Yoon, 2017
   * 
   * @param homogeneousSpace
   * @param hsTransport
   * @return */
  public static HermiteSubdivision standard(HomogeneousSpace homogeneousSpace) {
    return of(homogeneousSpace, HermiteLoConfig.STANDARD);
  }
}
