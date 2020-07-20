// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.Mahalanobis;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
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
public class LeverageDistances implements WeightingInterface, Serializable {
  /** @param vectorLogManifold
   * @return */
  public static WeightingInterface of(VectorLogManifold vectorLogManifold) {
    return new LeverageDistances(Objects.requireNonNull(vectorLogManifold));
  }

  /***************************************************/
  private final VectorLogManifold vectorLogManifold;

  private LeverageDistances(VectorLogManifold vectorLogManifold) {
    this.vectorLogManifold = vectorLogManifold;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return new Mahalanobis(vectorLogManifold.logAt(point), sequence).leverages_sqrt();
  }
}
