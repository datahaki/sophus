// code by jph
package ch.alpine.sophus.bm;

import ch.alpine.tensor.Tensor;

public interface MeanEstimate {
  Tensor estimate(Tensor sequence, Tensor weights);
}
