// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;

/** Careful: this is not a norm but an ad-invariant scalar product
 * that results in biinvariant barycentric coordinates */
public enum HeAdNorm implements TensorNorm {
  INSTANCE;

  @Override // from TensorNorm
  public Scalar norm(Tensor tensor) {
    Tensor vector = Flatten.of(tensor);
    return RnNorm.INSTANCE.norm(vector.extract(0, vector.length() - 1));
  }
}
