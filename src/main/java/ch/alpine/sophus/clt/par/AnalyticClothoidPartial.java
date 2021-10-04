// code by jph
package ch.alpine.sophus.clt.par;

import ch.alpine.sophus.clt.LagrangeQuadratic;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.sca.Chop;

/* package */ enum AnalyticClothoidPartial {
  ;
  static final Chop CHOP = Chop._10;

  /** @param lagrangeQuadratic
   * @return */
  public static ClothoidPartial of(LagrangeQuadratic lagrangeQuadratic) {
    return of( //
        lagrangeQuadratic.c0(), //
        lagrangeQuadratic.c1(), //
        lagrangeQuadratic.c2());
  }

  /** @param c0
   * @param c1
   * @param c2
   * @return */
  public static ClothoidPartial of(Scalar c0, Scalar c1, Scalar c2) {
    if (Scalars.isZero(CHOP.apply(c2)))
      return Scalars.isZero(CHOP.apply(c1)) //
          ? new ClothoidPartialDegree0(c0)
          : new ClothoidPartialDegree1(c0, c1);
    return new ClothoidPartialDegree2(c0, c1, c2);
  }

  public static ClothoidPartial of(Number c0, Number c1, Number c2) {
    return of(RealScalar.of(c0), RealScalar.of(c1), RealScalar.of(c2));
  }
}
