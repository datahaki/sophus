// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.api.WeightingInterface;
import ch.alpine.sophus.bm.BiinvariantMean;

/** The barycentric coordinate is the "INVERSE" of computing a weighted average.
 * 
 * A barycentric coordinate is a {@link WeightingInterface} that reproduces the identity.
 * 
 * Quote from "Weighted Averages on Surfaces" by Panozzo et al., 2013:
 * "We address both the forward problem, namely computing an average of given anchor points
 * on the mesh with given weights, and the inverse problem, which is computing the weights
 * given anchor points and a target point."
 * 
 * @see BiinvariantMean */
@FunctionalInterface
public interface BarycentricCoordinate extends WeightingInterface {
  // ---
}
