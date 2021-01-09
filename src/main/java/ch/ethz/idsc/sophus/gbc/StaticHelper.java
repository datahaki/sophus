// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.InfluenceMatrix;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
/* package */ enum StaticHelper {
  ;
  /** function returns a vector vnull that satisfies
   * vnull . design == 0
   * 
   * @param design matrix
   * @param vector
   * @return mapping of vector to solution of barycentric equation */
  public static Tensor barycentric(Tensor design, Tensor vector) {
    return NormalizeTotal.FUNCTION.apply(InfluenceMatrix.of(design).kernel(vector));
  }
}
