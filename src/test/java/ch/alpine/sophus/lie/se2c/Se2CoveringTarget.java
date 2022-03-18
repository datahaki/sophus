// code by jph
package ch.alpine.sophus.lie.se2c;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.sca.Sign;

/** only for testing ad-invariance */
public class Se2CoveringTarget implements TensorNorm, Serializable {
  private final TensorNorm tensorNorm;
  private final Scalar offset;

  /** @param tensorNorm either {@link Vector2Norm} or {@link Vector2NormSquared}
   * @param offset */
  public Se2CoveringTarget(TensorNorm tensorNorm, Scalar offset) {
    this.tensorNorm = Objects.requireNonNull(tensorNorm);
    this.offset = Sign.requirePositiveOrZero(offset);
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor uvw) {
    return tensorNorm.norm(uvw.extract(2, 3)).add(offset);
  }
}
