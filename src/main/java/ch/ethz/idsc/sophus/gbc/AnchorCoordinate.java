// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.AnchorDistances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** anchor coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public class AnchorCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new AnchorCoordinate(vectorLogManifold, variogram);
  }

  /***************************************************/
  private final AnchorDistances anchorDistances;

  private AnchorCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    anchorDistances = new AnchorDistances(vectorLogManifold, variogram);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return anchorDistances.biinvariantVector(sequence, point).coordinate();
  }
}
