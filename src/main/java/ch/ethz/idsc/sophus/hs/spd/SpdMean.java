// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.ArgMaxBiinvariantMean;
import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

// TODO tests!
public class SpdMean extends IterativeBiinvariantMean {
  public static final BiinvariantMean INSTANCE = new SpdMean(Chop._10);

  public SpdMean(Chop chop) {
    super(SpdManifold.INSTANCE, ArgMaxBiinvariantMean.INSTANCE, chop);
  }
}
