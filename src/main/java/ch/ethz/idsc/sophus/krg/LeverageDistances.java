// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.hs.Mahalanobis;
import ch.ethz.idsc.tensor.Tensor;

/** leverage distances are biinvariant
 * 
 * computes form at given point based on points in sequence and returns
 * vector of evaluations dMah_x(p_i) of points in sequence.
 * 
 * target distances are identical to anchor distances
 * 
 * <p>References:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020
 * 
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public enum LeverageDistances implements Genesis {
  INSTANCE;

  @Override // from WeightingInterface
  public Tensor origin(Tensor levers) {
    return new Mahalanobis(levers).leverages_sqrt();
  }
}
