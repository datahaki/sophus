// code by jph
package ch.ethz.idsc.sophus.clt.par;

import ch.ethz.idsc.sophus.clt.LagrangeQuadratic;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.sca.Chop;

/* package */ enum AnalyticPartial {
  ;
  static final Chop CHOP = Chop._10;

  /** @param lagrangeQuadratic
   * @return */
  static PartialInterface of(LagrangeQuadratic lagrangeQuadratic) {
    return of( //
        lagrangeQuadratic.c0(), //
        lagrangeQuadratic.c1(), //
        lagrangeQuadratic.c2());
  }

  /** @param c0
   * @param c1
   * @param c2
   * @return */
  /* package */ static PartialInterface of(Scalar c0, Scalar c1, Scalar c2) {
    if (Scalars.isZero(CHOP.apply(c2)))
      return Scalars.isZero(CHOP.apply(c1)) //
          ? new PartialDegree0(c0)
          : new PartialDegree1(c0, c1);
    return new PartialDegree2(c0, c1, c2);
  }
}
