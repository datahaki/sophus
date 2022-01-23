// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

import ch.alpine.sophus.dv.HarborBiinvariantVector;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** immutable
 * 
 * @see HarborBiinvariantVector */
public record BiinvariantVector(InfluenceMatrix influenceMatrix, Tensor vector) implements Serializable {
  /** @return vector of affine weights */
  public Tensor weighting(ScalarUnaryOperator variogram) {
    return NormalizeTotal.FUNCTION.apply(vector.map(variogram));
  }

  /** @return generalized barycentric coordinate */
  public Tensor coordinate(ScalarUnaryOperator variogram) {
    return NormalizeTotal.FUNCTION.apply(influenceMatrix.kernel(weighting(variogram)));
  }
}
