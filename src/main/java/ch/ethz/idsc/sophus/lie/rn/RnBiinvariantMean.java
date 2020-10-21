// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.BiinvariantMeans;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Integers;

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
