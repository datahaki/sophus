// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.math.LeftSpan;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeastSquares;

/** @see LeastSquares */
/* package */ enum StaticHelper {
  ;
  /** function returns a vector vnull that satisfies
   * vnull . matrix == 0
   * 
   * @param vector
   * @param matrix
   * @return barycentric projection of vector */
  public static Tensor barycentric(Tensor vector, Tensor matrix) {
    return NormalizeTotal.FUNCTION.apply(LeftSpan.kernel(vector, matrix));
  }
}
