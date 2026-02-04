// code by jph
package ch.alpine.sophus.bm;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.red.Mean;

/** @see Mean */
public record CenterMean(BiinvariantMean biinvariantMean) implements TensorUnaryOperator {
  @Override
  public Tensor apply(Tensor sequence) {
    int n = sequence.length();
    return biinvariantMean.mean(sequence, ConstantArray.of(RationalScalar.of(1, n), n));
  }
}
