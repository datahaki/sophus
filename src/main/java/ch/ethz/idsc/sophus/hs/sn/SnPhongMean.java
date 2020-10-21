// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;

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

  /** R^n+1 -> S^n */
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return NORMALIZE.apply(AffineQ.require(weights).dot(sequence));
  }
}
