// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/* package */ class InverseDiagonal implements TensorUnaryOperator {
  /** @param tensorMetric non-null
   * @return */
  public static TensorUnaryOperator of(ScalarUnaryOperator variogram) {
    return new InverseDiagonal(Objects.requireNonNull(variogram));
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;

  private InverseDiagonal(ScalarUnaryOperator variogram) {
    this.variogram = variogram;
  }

  @Override
  public Tensor apply(Tensor tensor) {
    Tensor weights = Tensors.reserve(tensor.length());
    int index = 0;
    for (Tensor p : tensor) {
      weights.append(variogram.apply(Norm._2.ofVector(p.extract(index, index + 1))));
      ++index;
    }
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}
