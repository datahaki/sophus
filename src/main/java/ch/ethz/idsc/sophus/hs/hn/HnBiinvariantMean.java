// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

public class HnBiinvariantMean extends IterativeBiinvariantMean {
  public static final BiinvariantMean INSTANCE = new HnBiinvariantMean(Chop._05);

  public HnBiinvariantMean(Chop chop) {
    super(HnManifold.INSTANCE, HnPhongMean.INSTANCE, chop);
  }
}
