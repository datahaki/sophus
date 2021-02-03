// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;

import ch.ethz.idsc.sophus.dv.HarborDistances;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;

/** immutable
 * 
 * @see HarborDistances */
public class BiinvariantVector implements Serializable {
  private static final long serialVersionUID = -1348113806577012382L;
  // ---
  private final Tensor matrix;
  private final Tensor vector;

  /** @param influenceMatrix square and symmetric
   * @param vector */
  public BiinvariantVector(Tensor influenceMatrix, Tensor vector) {
    this.matrix = influenceMatrix;
    this.vector = vector;
  }

  /** @return */
  public Tensor distances() {
    return vector.copy();
  }

  /** @return vector of affine weights */
  public Tensor weighting(ScalarUnaryOperator variogram) {
    return NormalizeTotal.FUNCTION.apply(vector.map(variogram));
  }

  /** @return generalized barycentric coordinate */
  public Tensor coordinate(ScalarUnaryOperator variogram) {
    Tensor weights = weighting(variogram);
    return NormalizeTotal.FUNCTION.apply(weights.subtract(matrix.dot(weights)));
  }
}
