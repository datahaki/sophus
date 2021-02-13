// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.VectorNorm2Squared;

public enum RnNormSquared implements TensorNorm {
  INSTANCE;

  @Override // from TensorNorm
  public Scalar norm(Tensor vector) {
    return VectorNorm2Squared.of(vector);
  }
}
