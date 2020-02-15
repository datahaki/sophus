// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;

/** Inverse Distance Coordinates are generalized barycentric coordinates with the properties
 * partition of unity
 * linear reproduction
 * Lagrange
 * C^infinity (except at points from input set)
 * 
 * in general, the coordinates may evaluate to be negative
 * 
 * Reference:
 * "Inverse Distance Coordinates for Scattered Sets of Points"
 * by Hakenberg, 2020, http://vixra.org/abs/2002.0129
 * 
 * @see BiinvariantMean */
public interface BarycentricCoordinate {
  /** @param sequence
   * @param mean
   * @return vector of affine weights corresponding to given mean
   * @see AffineQ */
  Tensor weights(Tensor sequence, Tensor mean);
}
