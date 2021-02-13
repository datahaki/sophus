// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.VectorNorm1;

public enum ScVectorNorm implements TensorNorm {
  INSTANCE;

  @Override // from TensorNorm
  public Scalar norm(Tensor tensor) {
    return VectorNorm1.of(tensor);
  }
}
