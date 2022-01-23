// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.PoleLadder;

public enum HnTransport {
  ;
  public static final HsTransport INSTANCE = new PoleLadder(HnManifold.INSTANCE);
}
