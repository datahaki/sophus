// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

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
    return Vector2Norm.NORMALIZE.apply(weights.dot(sequence)); // R^(n+1) -> S^n
  }
}
