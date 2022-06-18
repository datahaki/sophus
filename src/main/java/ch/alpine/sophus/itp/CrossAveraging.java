// code by jph
package ch.alpine.sophus.itp;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

public record CrossAveraging(Sedarim sedarim, BiinvariantMean biinvariantMean, Tensor values) //
    implements TensorUnaryOperator {
  @Override
  public Tensor apply(Tensor point) {
    return biinvariantMean.mean(values, sedarim.sunder(point));
  }
}
