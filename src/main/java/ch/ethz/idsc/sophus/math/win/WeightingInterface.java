// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;

/** weighting without linear reproduction property, otherwise use {@link BarycentricCoordinate} */
public interface WeightingInterface {
  /** @param sequence
   * @param point
   * @return vector of affine weights corresponding to given point
   * @see AffineQ */
  Tensor weights(Tensor sequence, Tensor point);
}
