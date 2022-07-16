// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.red.Times;

public enum Hermite2Subdivisions {
  ;
  private static final Scalar _1_8 = RationalScalar.of(1, 8);

  /** References:
   * "de Rham Transform of a Hermite Subdivision Scheme"
   * by Dubuc, Merrien, 2007, p. 9
   * 
   * "From Hermite to Stationary Subdivision Schemes in One and Several Variables"
   * by Merrien, Sauer, 2010, p. 27
   * 
   * "Dual Hermite subdivision schemes of de Rham-type"
   * by Conti, Merrien, Romani, 2015, p. 11
   * 
   * "Extended Hermite Subdivision Schemes"
   * by Merrien, Sauer, 2016, p. 17
   * 
   * "An algebraic approach to polynomial reproduction of hermite subdivision schemes"
   * by Conti, Huening, 2018, p. 14
   * 
   * @param homogeneousSpace
   * @param lambda
   * @param mu
   * @return */
  public static HermiteSubdivision of(HomogeneousSpace homogeneousSpace, HermiteLoParam hermiteLoParam) {
    Scalar lambda = hermiteLoParam.lambda;
    Scalar mu = hermiteLoParam.mu;
    Scalar an2_11 = RealScalar.TWO.add(Times.of(RealScalar.of(4), lambda, RealScalar.ONE.subtract(mu)));
    Scalar an2_12 = Times.of(RealScalar.TWO, lambda, RealScalar.TWO.add(mu));
    Scalar an2_21 = Polynomial.of(Tensors.vector(4, -2, -2)).apply(mu);
    Scalar an2_22 = mu.multiply(mu).add(Times.of(RealScalar.of(8), lambda, RealScalar.ONE.subtract(mu)));
    Tensor ALQ = Tensors.of(Tensors.of(an2_11, an2_12), Tensors.of(an2_21, an2_22)).multiply(_1_8);
    // ---
    Scalar an1_11 = RealScalar.of(6).subtract(Times.of(RealScalar.of(4), lambda, RealScalar.ONE.subtract(mu)));
    Scalar an1_12 = Times.of(RealScalar.of(-2), lambda, RealScalar.of(-4).add(mu));
    Scalar an1_21 = an2_21;
    Scalar an1_22 = mu.multiply(mu).subtract(Times.of(RealScalar.of(8), lambda, RealScalar.ONE.subtract(mu))).add(mu).add(mu);
    Tensor ALP = Tensors.of(Tensors.of(an1_11, an1_12.negate()), Tensors.of(an1_21.negate(), an1_22)).multiply(_1_8);
    return new Hermite2Subdivision( //
        homogeneousSpace, //
        ALQ.Get(0, 0), // lgg
        ALP.Get(0, 1), // lgv
        ALQ.Get(0, 1), // hgv
        ALQ.Get(1, 0), // hvg
        Tensors.of(ALP.Get(1, 1), ALQ.Get(1, 1))); // vpq
  }

  // ---
  /** lambda == -1/8, mu == -1/2
   * These parameters lead to the reproduction of P3 for the H1 scheme.
   * 
   * <p>Reference:
   * "Increasing the smoothness of vector and Hermite subdivision schemes"
   * Example 45, p. 25
   * by Moosmueller, Dyn, 2017
   * 
   * @param homogeneousSpace
   * @return
   * @see Hermite1Subdivision */
  public static HermiteSubdivision standard(HomogeneousSpace homogeneousSpace) {
    return of(homogeneousSpace, HermiteLoParam.STANDARD);
  }

  // ---
  /** lambda == -1/5, mu == 9/10
   * 
   * <p>Reference:
   * "Hermite subdivision on manifolds via parallel transport"
   * Example 1, p. 1063
   * by Moosmueller, 2017
   * 
   * @param homogeneousSpace
   * @return */
  public static HermiteSubdivision manifold(HomogeneousSpace homogeneousSpace) {
    return of(homogeneousSpace, HermiteLoParam.MANIFOLD);
  }
}
