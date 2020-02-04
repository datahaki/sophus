// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;

public class InverseDistanceFromOrigin implements TensorUnaryOperator {
  private static final Chop CHOP = Chop._14;
  // ---
  private final TensorNorm tensorNorm;

  /** @param tensorMetric non-null */
  public InverseDistanceFromOrigin(TensorNorm tensorNorm) {
    this.tensorNorm = Objects.requireNonNull(tensorNorm);
  }

  @Override
  public Tensor apply(Tensor tensor) {
    Tensor weights = Tensors.reserve(tensor.length());
    int count = 0;
    for (Tensor p : tensor) {
      Scalar distance = tensorNorm.norm(p);
      if (CHOP.allZero(distance))
        return UnitVector.of(tensor.length(), count);
      weights.append(distance.reciprocal());
      ++count;
    }
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}
