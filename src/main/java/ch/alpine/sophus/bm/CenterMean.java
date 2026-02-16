// code by jph
package ch.alpine.sophus.bm;

import ch.alpine.sophus.math.AveragingWeights;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.red.Mean;

/** @see Mean */
public record CenterMean(BiinvariantMean biinvariantMean) implements TensorUnaryOperator {
  @Override
  public Tensor apply(Tensor sequence) {
    return biinvariantMean.mean(sequence, AveragingWeights.of(sequence.length()));
  }
}
