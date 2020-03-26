// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.sca.Chop;

/* package */ interface PartialInterface {
  static final Chop CHOP = Chop._10;

  /** @param c0
   * @param c1
   * @param c2
   * @return */
  static PartialInterface of(Scalar c0, Scalar c1, Scalar c2) {
    if (Scalars.isZero(CHOP.apply(c2)))
      return Scalars.isZero(CHOP.apply(c1)) //
          ? new PartialDegree0(c0)
          : new PartialDegree1(c0, c1);
    return new PartialDegree2(c0, c1, c2);
  }

  /** @param t
   * @return approximate integration of exp i*series(c0, c1, c2) on [0, t] */
  Scalar il(Scalar t);
}
