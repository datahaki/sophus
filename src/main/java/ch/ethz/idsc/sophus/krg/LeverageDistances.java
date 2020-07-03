// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

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
 * <p>Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public class LeverageDistances implements WeightingInterface, Serializable {
  /** @param vectorLogManifold
   * @return */
  public static WeightingInterface of(VectorLogManifold vectorLogManifold) {
    return new LeverageDistances(vectorLogManifold);
  }

  /***************************************************/
  private final Mahalanobis mahalanobis;

  private LeverageDistances(VectorLogManifold vectorLogManifold) {
    mahalanobis = new Mahalanobis(vectorLogManifold);
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return mahalanobis.new Form(sequence, point).leverages();
  }
}
