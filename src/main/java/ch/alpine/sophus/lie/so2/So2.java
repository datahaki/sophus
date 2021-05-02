// code by jph
package ch.alpine.sophus.lie.so2;

import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Mod;

public enum So2 {
  ;
  /** maps values to the semi-open interval [-pi, pi) */
  public static final Mod MOD = Mod.function(Pi.TWO, Pi.VALUE.negate());
}
