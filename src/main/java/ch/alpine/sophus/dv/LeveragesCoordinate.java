// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** leverage coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public enum LeveragesCoordinate {
  ;
  /** @param hsDesign
   * @param manifold
   * @param variogram for instance InversePowerVariogram.of(2)
   * @return */
  public static BarycentricCoordinate of(HsDesign hsDesign, ScalarUnaryOperator variogram) {
    return new HsCoordinates(hsDesign, new LeveragesGenesis(variogram));
  }
}
