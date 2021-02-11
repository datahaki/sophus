// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.VectorNormInterface;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** hyperboloid model
 * norm for tangent vectors
 * 
 * "Metric Spaces of Non-Positive Curvature"
 * by Martin R. Bridson, Andre Haefliger, 1999 */
public enum HnNorm implements VectorNormInterface {
  INSTANCE;

  @Override // from TensorNorm
  public Scalar ofVector(Tensor v) {
    // norm does not depend on base point (which is the case also for S^n)
    Scalar n2 = LBilinearForm.normSquared(v);
    if (Sign.isPositiveOrZero(n2))
      return Sqrt.FUNCTION.apply(n2);
    Chop._08.requireZero(n2);
    return n2.zero();
  }
}
