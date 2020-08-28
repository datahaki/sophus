// code by jph
package ch.ethz.idsc.sophus.ref.d1h;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

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
  private static final Scalar N1_8 = RationalScalar.of(-1, 8);
  private static final Scalar N1_2 = RationalScalar.of(-1, 2);

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
   * @param hsExponential
   * @param hsTransport
   * @param lambda
   * @param mu
   * @return */
  public static HermiteSubdivision of(HsExponential hsExponential, HsTransport hsTransport, Scalar lambda, Scalar mu) {
    return new Hermite1Subdivision( //
        hsExponential, //
        Objects.requireNonNull(hsTransport), //
        lambda, //
        RealScalar.ONE.subtract(mu).multiply(RationalScalar.HALF), //
        mu.multiply(_1_4));
  }

  /** lambda == -1/8
   * mu == -1/2
   * 
   * Reference:
   * "Construction of Hermite subdivision schemes reproducing polynomials"
   * by Byeongseon Jeong, Jungho Yoon, 2017
   * 
   * @param hsExponential
   * @param hsTransport
   * @return */
  public static HermiteSubdivision standard(HsExponential hsExponential, HsTransport hsTransport) {
    return of(hsExponential, hsTransport, N1_8, N1_2);
  }
}
