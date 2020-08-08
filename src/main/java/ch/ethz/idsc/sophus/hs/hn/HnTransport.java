// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;

/** the pole ladder is exact in symmetric spaces
 * 
 * Reference:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * Nicolas Guigui, Xavier Pennec, 2020 */
public enum HnTransport {
  ;
  public static final HsTransport INSTANCE = PoleLadder.of(HnManifold.INSTANCE);
}
