// code by jph
package ch.ethz.idsc.sophus.itp;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class CrossAveraging implements TensorUnaryOperator {
  /** @param weightingInterface
   * @param sequence
   * @param biinvariantMean
   * @param values
   * @return */
  public static TensorUnaryOperator of( //
      WeightingInterface weightingInterface, Tensor sequence, BiinvariantMean biinvariantMean, Tensor values) {
    return new CrossAveraging( //
        Objects.requireNonNull(weightingInterface), //
        Objects.requireNonNull(sequence), //
        Objects.requireNonNull(biinvariantMean), //
        Objects.requireNonNull(values));
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;
  private final Tensor sequence;
  private final BiinvariantMean biinvariantMean;
  private final Tensor values;

  private CrossAveraging(WeightingInterface weightingInterface, Tensor sequence, BiinvariantMean biinvariantMean, Tensor values) {
    this.weightingInterface = weightingInterface;
    this.sequence = sequence;
    this.biinvariantMean = biinvariantMean;
    this.values = values;
  }

  @Override
  public Tensor apply(Tensor point) {
    return biinvariantMean.mean(values, weightingInterface.weights(sequence, point));
  }
}
