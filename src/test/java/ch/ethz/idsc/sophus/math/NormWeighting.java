// code by jph
package ch.ethz.idsc.sophus.math;

import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public class NormWeighting implements TensorUnaryOperator {
  /** @param tensorNorm non-null
   * @param */
  public static TensorUnaryOperator of(TensorNorm tensorNorm, ScalarUnaryOperator variogram) {
    return new NormWeighting(Objects.requireNonNull(tensorNorm), Objects.requireNonNull(variogram));
  }

  /***************************************************/
  private final TensorNorm tensorNorm;
  private final ScalarUnaryOperator variogram;

  private NormWeighting(TensorNorm tensorNorm, ScalarUnaryOperator variogram) {
    this.tensorNorm = tensorNorm;
    this.variogram = variogram;
  }

  @Override
  public Tensor apply(Tensor tensor) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(tensor.stream() //
        .map(tensorNorm::norm) //
        .map(variogram)));
  }
}
