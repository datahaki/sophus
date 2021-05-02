// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.BiinvariantMeans;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;

/** Careful: The weights are not checked to be affine.
 * 
 * @see AffineQ
 * @see BiinvariantMeans */
public enum RnBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Integers.requirePositive(weights.length());
    return weights.dot(sequence);
  }
}
