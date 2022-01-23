// code by ob, jph
package ch.alpine.sophus.flt.bm;

import java.util.Objects;
import java.util.function.Function;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** BiinvariantMeanCenter projects a uniform sequence of points to their extrapolate
 * with each point weighted as provided by an external function.
 * 
 * @param biinvariantMean non-null
 * @param function non-null
 * @return
 * @throws Exception if either input parameter is null */
public record BiinvariantMeanExtrapolation(BiinvariantMean biinvariantMean, Function<Integer, Tensor> function) implements TensorUnaryOperator {
  public BiinvariantMeanExtrapolation {
    Objects.requireNonNull(biinvariantMean);
    Objects.requireNonNull(function);
  }

  @Override
  public Tensor apply(Tensor sequence) {
    return biinvariantMean.mean(sequence, function.apply(sequence.length()));
  }
}