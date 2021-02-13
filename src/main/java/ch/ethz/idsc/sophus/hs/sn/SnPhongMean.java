// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;
import ch.ethz.idsc.tensor.sca.Chop;

/** Phong projection is faster than {@link SnBiinvariantMean}.
 * However, Phong projection is not the inverse to inverse distance coordinates.
 * 
 * Reference:
 * "Illumination for computer generated pictures"
 * by Bui Tuong Phong, 1975
 * 
 * Reference:
 * "Weighted Averages on Surfaces"
 * by Daniele Panozzo, Ilya Baran, Olga Diamanti, Olga Sorkine-Hornung */
public enum SnPhongMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    // R^n+1 -> S^n
    return VectorNorm2.NORMALIZE.apply(AffineQ.require(weights, Chop._08).dot(sequence));
  }
}
