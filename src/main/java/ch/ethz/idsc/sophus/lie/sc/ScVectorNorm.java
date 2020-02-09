// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;

public enum ScVectorNorm implements TensorNorm {
  INSTANCE;

  @Override
  public Scalar norm(Tensor tensor) {
    return Norm._1.ofVector(tensor);
  }
}
