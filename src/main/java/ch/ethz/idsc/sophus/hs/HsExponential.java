// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

@FunctionalInterface
public interface HsExponential {
  /** @param point
   * @return */
  Exponential exponential(Tensor point);
}
