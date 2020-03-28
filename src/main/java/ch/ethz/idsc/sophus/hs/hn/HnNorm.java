// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

public enum HnNorm implements TensorNorm {
  INSTANCE;

  @Override
  public Scalar norm(Tensor x) {
    return HnBilinearForm.between(x, x);
  }
}
