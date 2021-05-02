// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;

/** weighting without linear reproduction property, otherwise use BarycentricCoordinate */
@FunctionalInterface
public interface WeightingInterface {
  /** @param sequence
   * @param point
   * @return vector of affine weights corresponding to given point
   * @see AffineQ */
  Tensor weights(Tensor sequence, Tensor point);
}
