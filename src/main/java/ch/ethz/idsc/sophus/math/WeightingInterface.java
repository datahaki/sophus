// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;

/** weighting without linear reproduction property, otherwise use BarycentricCoordinate */
@FunctionalInterface
public interface WeightingInterface {
  /** @param sequence
   * @param point
   * @return vector of affine weights corresponding to given point
   * @see AffineQ */
  Tensor weights(Tensor sequence, Tensor point);
}
