// code by jph
package ch.alpine.sophus.crv.clt.par;

import ch.alpine.tensor.Scalar;

@FunctionalInterface
/* package */ interface ClothoidPartial {
  /** @param t
   * @return approximate integration of exp i*series(c0, c1, c2) on [0, t] */
  Scalar il(Scalar t);
}
