// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;

/** leverage coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public enum LeveragesCoordinate {
  ;
  /** computes leverage coordinates via Mahalanobis distance which requires the computation of
   * two pseudo inverses but smaller matrix dot products then when building the influence matrix.
   * 
   * @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return HsCoordinates.wrap(vectorLogManifold, LeveragesGenesis.of(variogram));
  }
}
