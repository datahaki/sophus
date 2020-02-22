// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.NumberQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.sca.Sqrt;

public class InverseBiNorm implements Serializable {
  /** @param tensorMetric non-null */
  public static InverseBiNorm of(TensorNorm tensorNorm) {
    return new InverseBiNorm(Objects.requireNonNull(tensorNorm));
  }

  /***************************************************/
  private final TensorNorm tensorNorm;

  private InverseBiNorm(TensorNorm tensorNorm) {
    this.tensorNorm = tensorNorm;
  }

  public Tensor binorm(Tensor levers1, Tensor levers2) {
    Tensor weights = Tensors.reserve(levers1.length());
    for (int index = 0; index < levers1.length(); ++index) {
      Tensor p1 = levers1.get(index);
      Tensor p2 = levers2.get(index);
      Scalar r1 = tensorNorm.norm(p1).reciprocal();
      Scalar r2 = tensorNorm.norm(p2).reciprocal();
      if (!NumberQ.of(r1) || !NumberQ.of(r2))
        return UnitVector.of(levers1.length(), index);
      weights.append(Sqrt.FUNCTION.apply(r1.multiply(r2)));
    }
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}
