// code by jph
package ch.alpine.sophus.bm;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** derived from BiinvariantMean but with return type {@link Scalar} */
@FunctionalInterface
public interface ScalarBiinvariantMean extends BiinvariantMean {
  @Override // from BiinvariantMean
  Scalar mean(Tensor sequence, Tensor weights);
}
