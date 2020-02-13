// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;

public enum RnNorm implements TensorNorm {
  INSTANCE;

  @Override
  public Scalar norm(Tensor vector) {
    return Norm._2.ofVector(vector);
  }
}
