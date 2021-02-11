// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** for a point in H^n the HnNormSquared equals -1 */
public enum HnNormSquared implements TensorNorm {
  INSTANCE;

  @Override // from TensorNorm
  public Scalar norm(Tensor x) {
    // TODO what is this: distance from what point/norm of which vector!?
    return HnBilinearForm.between(x, x);
  }
}
