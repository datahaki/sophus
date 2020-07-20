// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;

import ch.ethz.idsc.sophus.krg.HarborDistances;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** immutable
 * 
 * @see HarborDistances */
public class BiinvariantVector implements Serializable {
  private final Tensor matrix;
  private final Tensor vector;

  public BiinvariantVector(HsInfluence hsInfluence, Tensor vector) {
    this.matrix = hsInfluence.matrix();
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
