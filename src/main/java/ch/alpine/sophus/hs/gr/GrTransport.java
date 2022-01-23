// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.PoleLadder;

public enum GrTransport {
  ;
  public static final HsTransport INSTANCE = new PoleLadder(GrManifold.INSTANCE);
}
