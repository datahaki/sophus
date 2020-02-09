// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.st.StBiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;

/** 1-parameter Scaling Group (R+, *)
 * 
 * @see StBiinvariantMean */
public enum ScBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from ScalarBiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return ScExponential.INSTANCE.exp(weights.dot(Tensor.of(sequence.stream().map(ScExponential.INSTANCE::log))));
  }
}
