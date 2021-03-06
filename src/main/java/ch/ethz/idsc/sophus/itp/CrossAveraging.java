// code by jph
package ch.ethz.idsc.sophus.itp;

import java.util.Objects;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

public class CrossAveraging implements TensorUnaryOperator {
  /** @param tensorUnaryOperator
   * @param biinvariantMean
   * @param values
   * @return */
  public static TensorUnaryOperator of( //
      TensorUnaryOperator tensorUnaryOperator, BiinvariantMean biinvariantMean, Tensor values) {
    return new CrossAveraging( //
        Objects.requireNonNull(tensorUnaryOperator), //
        Objects.requireNonNull(biinvariantMean), //
        Objects.requireNonNull(values));
  }

  /***************************************************/
  private final TensorUnaryOperator tensorUnaryOperator;
  private final BiinvariantMean biinvariantMean;
  private final Tensor values;

  private CrossAveraging(TensorUnaryOperator tensorUnaryOperator, BiinvariantMean biinvariantMean, Tensor values) {
    this.tensorUnaryOperator = tensorUnaryOperator;
    this.biinvariantMean = biinvariantMean;
    this.values = values;
  }

  @Override
  public Tensor apply(Tensor point) {
    return biinvariantMean.mean(values, tensorUnaryOperator.apply(point));
  }
}
