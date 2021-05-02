// code by jph
package ch.alpine.sophus.lie.so2c;

import ch.alpine.sophus.lie.ScalarBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** The covering of SO(2) is isometric to R.
 * There are no restrictions on the input sequence.
 * 
 * @see RnBiinvariantMean */
public enum So2CoveringBiinvariantMean implements ScalarBiinvariantMean {
  INSTANCE;

  @Override // from ScalarBiinvariantMean
  public Scalar mean(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    return (Scalar) weights.dot(sequence);
  }
}
