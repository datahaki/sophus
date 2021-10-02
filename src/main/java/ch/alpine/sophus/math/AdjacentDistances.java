// code by jph
package ch.alpine.sophus.math;

import java.util.Objects;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.alg.AdjacentReduce;

/** implementation taken from {@link Differences} */
public class AdjacentDistances extends AdjacentReduce {
  private final TensorMetric tensorMetric;

  public AdjacentDistances(TensorMetric tensorMetric) {
    this.tensorMetric = Objects.requireNonNull(tensorMetric);
  }

  @Override
  protected Tensor reduce(Tensor prev, Tensor next) {
    return tensorMetric.distance(prev, next);
  }
}
