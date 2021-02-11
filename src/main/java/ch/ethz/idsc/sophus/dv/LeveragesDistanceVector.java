// code by jph
package ch.ethz.idsc.sophus.dv;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Mahalanobis;

/** leverages distances are biinvariant
 * 
 * <p>computes form at given point based on points in sequence and returns
 * vector of evaluations dMah_x(p_i) of points in sequence.
 * 
 * <p>one evaluation of the leverages involves the computation of
 * <pre>
 * PseudoInverse[levers^T . levers]
 * </pre>
 * 
 * <p>References:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020
 * 
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public enum LeveragesDistanceVector implements Genesis {
  INSTANCE;

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return new Mahalanobis(levers).leverages_sqrt();
  }
}
