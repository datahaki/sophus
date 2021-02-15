// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.nrm.Normalize;
import ch.ethz.idsc.tensor.sca.Ramp;

/** hyperboloid model
 * norm for tangent vectors
 * 
 * "Metric Spaces of Non-Positive Curvature"
 * by Martin R. Bridson, Andre Haefliger, 1999 */
public enum HnVectorNorm {
  ;
  public static final TensorUnaryOperator NORMALIZE = Normalize.with(HnVectorNorm::of);

  /** @param v tangent vector
   * @return */
  public static Scalar of(Tensor v) {
    return HnHypot.of(v, Ramp.FUNCTION);
  }
}
