// code by jph
package ch.alpine.sophus.math;

import java.util.Objects;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** ONLY FOR TESTING */
public class NormWeighting implements Genesis {
  /** @param tensorNorm non-null
   * @param */
  public static Genesis of(TensorNorm tensorNorm, ScalarUnaryOperator variogram) {
    return new NormWeighting(Objects.requireNonNull(tensorNorm), Objects.requireNonNull(variogram));
  }

  // ---
  private final TensorNorm tensorNorm;
  private final ScalarUnaryOperator variogram;

  private NormWeighting(TensorNorm tensorNorm, ScalarUnaryOperator variogram) {
    this.tensorNorm = tensorNorm;
    this.variogram = variogram;
  }

  @Override
  public Tensor origin(Tensor tensor) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(tensor.stream() //
        .map(tensorNorm::norm) //
        .map(variogram)));
  }
}
