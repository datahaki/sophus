// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** hyperboloid model
 * norm for tangent vectors
 * 
 * "Metric Spaces of Non-Positive Curvature"
 * by Martin R. Bridson, Andre Haefliger, 1999 */
public enum HnNorm implements TensorNorm {
  INSTANCE;

  @Override // from TensorNorm
  public Scalar norm(Tensor v) {
    Scalar n2 = HnNormSquared.INSTANCE.norm(v);
    if (Sign.isPositiveOrZero(n2))
      return Sqrt.FUNCTION.apply(n2);
    Chop._06.requireZero(n2);
    return n2.zero();
  }
}
