// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.InfluenceMatrix;
import ch.ethz.idsc.tensor.mat.LeastSquares;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see LeastSquares */
/* package */ enum StaticHelper {
  ;
  /** function returns a vector vnull that satisfies
   * vnull . matrix == 0
   * 
   * @param vector
   * @param design matrix
   * @return mapping of vector to solution of barycentric equation */
  public static Tensor barycentric(Tensor vector, Tensor design) {
    return NormalizeTotal.FUNCTION.apply(InfluenceMatrix.of(design).kernel(vector));
  }
}
