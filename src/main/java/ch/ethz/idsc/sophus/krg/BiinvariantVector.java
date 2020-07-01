// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;

/** immutable
 * 
 * @see AnchorDistances
 * @see HarborDistances */
public class BiinvariantVector implements Serializable {
  private final Tensor influence;
  private final Tensor vector;

  public BiinvariantVector(Tensor influence, Tensor vector) {
    this.influence = influence;
    this.vector = vector;
  }

  /** @return */
  public Tensor distances() {
    return vector.copy();
  }

  /** @return vector of affine weights */
  public Tensor weighting() {
    return NormalizeTotal.FUNCTION.apply(vector);
  }

  /** @return generalized barycentric coordinate */
  public Tensor coordinate() {
    Tensor weights = weighting();
    return NormalizeTotal.FUNCTION.apply(weights.subtract(influence.dot(weights)));
  }
}
