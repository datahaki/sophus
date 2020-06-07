// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;

/** immutable */
public class BiinvariantVector implements Serializable {
  private final Tensor projection;
  private final Tensor vector;

  public BiinvariantVector(Tensor projection, Tensor vector) {
    this.projection = projection;
    this.vector = vector;
  }

  public Tensor vector() {
    return vector.copy();
  }

  /** @return vector of affine weights */
  public Tensor normalized() {
    return NormalizeTotal.FUNCTION.apply(vector);
  }

  /** @return generalized barycentric coordinate */
  public Tensor coordinate() {
    return NormalizeTotal.FUNCTION.apply(normalized().dot(projection));
  }
}
