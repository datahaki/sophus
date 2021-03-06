// code by jph
package ch.ethz.idsc.sophus.lie.he;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.nrm.Vector2NormSquared;
import ch.ethz.idsc.tensor.sca.Sign;

/** Careful: this is not a norm but an ad-invariant, degenerate scalar product + offset
 * that results in biinvariant barycentric coordinates */
public class HeTarget implements TensorNorm, Serializable {
  private final TensorNorm tensorNorm;
  private final Scalar offset;

  /** @param tensorNorm either {@link Vector2Norm} or {@link Vector2NormSquared}
   * @param offset */
  public HeTarget(TensorNorm tensorNorm, Scalar offset) {
    this.tensorNorm = Objects.requireNonNull(tensorNorm);
    this.offset = Sign.requirePositiveOrZero(offset);
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor tensor) {
    Tensor vector = Flatten.of(tensor);
    return tensorNorm.norm(vector.extract(0, vector.length() - 1)).add(offset);
  }
}
