// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeastSquares;

/** @see LeastSquares */
/* package */ enum StaticHelper {
  ;
  /** @param levers
   * @param vector
   * @return barycentric projection */
  public static Tensor barycentric(Tensor levers, Tensor vector) {
    return NormalizeTotal.FUNCTION.apply(SpanProjection.kernel(levers, vector));
  }
}
