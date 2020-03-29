// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

public class SnMean extends IterativeBiinvariantMean {
  public static final BiinvariantMean INSTANCE = new SnMean(Chop._14);

  public SnMean(Chop chop) {
    super(SnManifold.INSTANCE, SnPhongMean.INSTANCE, chop);
  }
}
