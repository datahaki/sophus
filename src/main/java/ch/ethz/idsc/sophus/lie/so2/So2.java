// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.sca.Mod;

public enum So2 {
  ;
  /** maps values to the semi-open interval [-pi, pi) */
  public static final Mod MOD = Mod.function(Pi.TWO, Pi.VALUE.negate());
}
