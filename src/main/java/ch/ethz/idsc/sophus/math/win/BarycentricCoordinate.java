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
 * The barycentric coordinate is the "INVERSE" of computing a weighted average.
 * 
 * Quote from "Weighted Averages on Surfaces" by Panozzo et al., 2013:
 * "We address both the forward problem, namely computing an average of given anchor points
 * on the mesh with given weights, and the inverse problem, which is computing the weights
 * given anchor points and a target point."
 * 
 * @see BiinvariantMean */
public interface BarycentricCoordinate {
  /** @param sequence
   * @param point
   * @return vector of affine weights corresponding to given point
   * @see AffineQ */
  Tensor weights(Tensor sequence, Tensor point);
}
