// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;

import ch.ethz.idsc.sophus.dv.HarborBiinvariantVector;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.mat.gr.InfluenceMatrix;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;

/** immutable
 * 
 * @see HarborBiinvariantVector */
public class BiinvariantVector implements Serializable {
  private final InfluenceMatrix influenceMatrix;
  private final Tensor vector;

  /** @param influenceMatrix
   * @param vector */
  public BiinvariantVector(InfluenceMatrix influenceMatrix, Tensor vector) {
    this.influenceMatrix = influenceMatrix;
    this.vector = vector;
  }

  /** @return */
  public Tensor distances() {
    return vector;
  }

  /** @return vector of affine weights */
  public Tensor weighting(ScalarUnaryOperator variogram) {
    return NormalizeTotal.FUNCTION.apply(vector.map(variogram));
  }

  /** @return generalized barycentric coordinate */
  public Tensor coordinate(ScalarUnaryOperator variogram) {
    return NormalizeTotal.FUNCTION.apply(influenceMatrix.kernel(weighting(variogram)));
  }
}
