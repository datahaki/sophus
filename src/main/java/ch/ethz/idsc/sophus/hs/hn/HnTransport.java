// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;

public enum HnTransport {
  ;
  public static final HsTransport INSTANCE = PoleLadder.of(HnManifold.INSTANCE);
}
