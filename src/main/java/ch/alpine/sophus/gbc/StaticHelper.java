// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.gr.Mahalanobis;
import ch.alpine.tensor.nrm.NormalizeTotal;

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
    // Mahalanobis is faster by a tiny amount than InfluenceMatrix[...] for this computation
    return NormalizeTotal.FUNCTION.apply(new Mahalanobis(design).kernel(vector));
  }
}
