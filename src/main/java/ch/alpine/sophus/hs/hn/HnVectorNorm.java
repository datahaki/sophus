// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.Normalize;
import ch.alpine.tensor.sca.Ramp;

/** hyperboloid model
 * norm for tangent vectors
 * 
 * "Metric Spaces of Non-Positive Curvature"
 * by Martin R. Bridson, Andre Haefliger, 1999 */
/* package */ enum HnVectorNorm {
  ;
  public static final TensorUnaryOperator NORMALIZE = Normalize.with(HnVectorNorm::of);

  /** @param v tangent vector
   * @return */
  public static Scalar of(Tensor v) {
    return HnHypot.of(v, Ramp.FUNCTION);
  }
}
