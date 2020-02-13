// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.NumberQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class InverseNorm implements TensorUnaryOperator {
  /** @param tensorMetric non-null */
  public static TensorUnaryOperator of(TensorNorm tensorNorm) {
    return new InverseNorm(Objects.requireNonNull(tensorNorm));
  }

  /***************************************************/
  private final TensorNorm tensorNorm;

  private InverseNorm(TensorNorm tensorNorm) {
    this.tensorNorm = tensorNorm;
  }

  @Override
  public Tensor apply(Tensor tensor) {
    Tensor weights = Tensors.reserve(tensor.length());
    int count = 0;
    for (Tensor p : tensor) {
      Scalar reciprocal = tensorNorm.norm(p).reciprocal();
      if (!NumberQ.of(reciprocal))
        return UnitVector.of(tensor.length(), count);
      weights.append(reciprocal);
      ++count;
    }
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}
