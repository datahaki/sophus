// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;
import ch.ethz.idsc.tensor.nrm.VectorNorm2Squared;
import ch.ethz.idsc.tensor.sca.Sign;

/** only for testing ad-invariance */
public class Se2CoveringTarget implements TensorNorm, Serializable {
  private final TensorNorm tensorNorm;
  private final Scalar offset;

  /** @param tensorNorm either {@link VectorNorm2} or {@link VectorNorm2Squared}
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
