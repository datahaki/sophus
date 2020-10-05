// code by jph
package ch.ethz.idsc.sophus.lie.he;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.sca.Sign;

/** Careful: this is not a norm but an ad-invariant, degenerate scalar product + offset
 * that results in biinvariant barycentric coordinates */
public class HeTarget implements TensorNorm, Serializable {
  private static final long serialVersionUID = 5493038101860965918L;
  // ---
  private final TensorNorm tensorNorm;
  private final Scalar offset;

  /** @param tensorNorm either {@link RnNorm} or {@link RnNormSquared}
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
