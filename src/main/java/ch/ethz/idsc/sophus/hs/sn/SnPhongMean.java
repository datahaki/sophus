// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;

/** Phong projection is faster than {@link SnMean}.
 * However, Phong projection is not the inverse to inverse distance coordinates.
 * 
 * Reference:
 * "Weighted Averages on Surfaces"
 * by Daniele Panozzo, Ilya Baran, Olga Diamanti, Olga Sorkine-Hornung */
public enum SnPhongMean implements BiinvariantMean {
  INSTANCE;

  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return NORMALIZE.apply(weights.dot(sequence));
  }
}