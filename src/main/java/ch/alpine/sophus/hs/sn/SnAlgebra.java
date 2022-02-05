// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.sophus.lie.so.SoAlgebra;

public enum SnAlgebra {
  ;
  public static HsAlgebra of(int d) {
    return new HsAlgebra(SoAlgebra.of(d + 1).ad(), d, 6);
  }
}
