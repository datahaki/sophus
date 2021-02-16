// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;

public enum GrTransport {
  ;
  public static final HsTransport INSTANCE = PoleLadder.of(GrManifold.INSTANCE);
}
