// code by jph
package ch.ethz.idsc.sophus.crv.hermite;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.rn.RnTransport;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Series;
import ch.ethz.idsc.tensor.red.Times;

public enum Hermite2Subdivisions {
  ;
  private static final Scalar _1_8 = RationalScalar.of(1, 8);
  private static final Scalar N1_8 = RationalScalar.of(-1, 8);
  private static final Scalar N1_2 = RationalScalar.of(-1, 2);
  private static final Scalar N1_5 = RationalScalar.of(-1, 5);
  private static final Scalar _9_10 = RationalScalar.of(9, 10);

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
   * @param lieGroup
   * @param exponential
   * @param lambda
   * @param mu
   * @return */
  public static HermiteSubdivision of(HsExponential hsExponential, HsTransport hsTransport, Scalar lambda, Scalar mu) {
    Scalar an2_11 = RealScalar.of(2).add(Times.of(RealScalar.of(4), lambda, RealScalar.ONE.subtract(mu)));
    Scalar an2_12 = Times.of(RealScalar.of(2), lambda, RealScalar.of(2).add(mu));
    Scalar an2_21 = Series.of(Tensors.vector(4, -2, -2)).apply(mu);
    Scalar an2_22 = mu.multiply(mu).add(Times.of(RealScalar.of(8), lambda, RealScalar.ONE.subtract(mu)));
    Tensor ALQ = Tensors.of(Tensors.of(an2_11, an2_12), Tensors.of(an2_21, an2_22)).multiply(_1_8);
    // ---
    Scalar an1_11 = RealScalar.of(6).subtract(Times.of(RealScalar.of(4), lambda, RealScalar.ONE.subtract(mu)));
    Scalar an1_12 = Times.of(RealScalar.of(-2), lambda, RealScalar.of(-4).add(mu));
    Scalar an1_21 = an2_21;
    Scalar an1_22 = mu.multiply(mu).subtract(Times.of(RealScalar.of(8), lambda, RealScalar.ONE.subtract(mu))).add(mu).add(mu);
    Tensor ALP = Tensors.of(Tensors.of(an1_11, an1_12.negate()), Tensors.of(an1_21.negate(), an1_22)).multiply(_1_8);
    return new Hermite2Subdivision(hsExponential, hsTransport, //
        ALQ.Get(0, 0), // lgg
        ALP.Get(0, 1), // lgv
        ALQ.Get(0, 1), // hgv
        ALQ.Get(1, 0), // hvg
        Tensors.of(ALP.Get(1, 1), ALQ.Get(1, 1))); // vpq
  }

  public static HermiteSubdivision of(LieGroup lieGroup, Exponential exponential, Scalar lambda, Scalar mu) {
    return of(LieExponential.of(lieGroup, exponential), RnTransport.INSTANCE, // FIXME
        lambda, mu);
  }

  /***************************************************/
  /** lambda == -1/8, mu == -1/2
   * These parameters lead to the reproduction of P3 for the H1 scheme.
   * 
   * <p>Reference:
   * "Increasing the smoothness of vector and Hermite subdivision schemes"
   * Example 45, p. 25
   * by Moosmueller, Dyn, 2017
   * 
   * @return
   * @see Hermite1Subdivision */
  public static HermiteSubdivision standard(HsExponential hsExponential, HsTransport hsTransport) {
    return of(hsExponential, hsTransport, N1_8, N1_2);
  }

  public static HermiteSubdivision standard(LieGroup lieGroup, Exponential exponential) {
    return of(lieGroup, exponential, N1_8, N1_2);
  }

  /***************************************************/
  /** lambda == -1/5, mu == 9/10
   * 
   * <p>Reference:
   * "Hermite subdivision on manifolds via parallel transport"
   * Example 1, p. 1063
   * by Moosmueller, 2017
   * 
   * @param lieGroup
   * @param exponential
   * @return */
  public static HermiteSubdivision manifold(LieGroup lieGroup, Exponential exponential) {
    return of(lieGroup, exponential, N1_5, _9_10);
  }
}
