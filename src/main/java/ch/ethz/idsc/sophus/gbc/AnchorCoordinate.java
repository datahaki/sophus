// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.AnchorDistances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** anchor coordinates are one implementation of leverage coordinates
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
/* package */ class AnchorCoordinate implements BarycentricCoordinate, Serializable {
  private final AnchorDistances anchorDistances;

  /** @param vectorLogManifold
   * @param variogram */
  public AnchorCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    anchorDistances = new AnchorDistances(vectorLogManifold, variogram);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return anchorDistances.biinvariantVector(sequence, point).coordinate();
  }
}
