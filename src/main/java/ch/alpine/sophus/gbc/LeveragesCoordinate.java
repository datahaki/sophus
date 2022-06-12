// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** leverage coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public enum LeveragesCoordinate {
  ;
  /** @param manifold
   * @param variogram for instance InversePowerVariogram.of(2)
   * @return */
  public static BarycentricCoordinate of(Manifold manifold, ScalarUnaryOperator variogram) {
    return HsCoordinates.of(manifold, new LeveragesGenesis(variogram));
  }
}
