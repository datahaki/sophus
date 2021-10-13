// code by jph
package ch.alpine.sophus.crv.dubins;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Mod;

/* package */ enum StaticHelper {
  ;
  private static final ScalarUnaryOperator MOD_TWO_PI = Mod.function(Pi.TWO);

  public static Scalar principalValue(Scalar angle) {
    return MOD_TWO_PI.apply(angle);
  }

  // ---
  private static final Scalar NUMERIC_ONE = RealScalar.of(1.0);

  /** @param scalar
   * @return true if given scalar is greater than 1 */
  public static boolean greaterThanOne(Scalar scalar) {
    return Scalars.lessThan(NUMERIC_ONE, scalar);
  }
}
