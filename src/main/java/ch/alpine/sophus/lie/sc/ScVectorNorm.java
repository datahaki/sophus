// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector1Norm;

public enum ScVectorNorm implements TensorNorm {
  INSTANCE;

  @Override // from TensorNorm
  public Scalar norm(Tensor tensor) {
    return Vector1Norm.of(tensor);
  }
}
