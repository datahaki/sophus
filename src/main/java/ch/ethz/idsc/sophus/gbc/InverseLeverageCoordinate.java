// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** inverse leverage coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public enum InverseLeverageCoordinate {
  ;
  /** computes inverse leverage coordinates via Mahalanobis distance which requires the computation of
   * two pseudo inverses but smaller matrix dot products then when building the influence matrix.
   * 
   * @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate fast(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new TargetCoordinate(vectorLogManifold, variogram);
  }

  /** computes inverse leverage coordinates via influence matrix which requires the computation of one
   * pseudo inverse but a large matrix dot product.
   * 
   * @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate slow(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new AnchorCoordinate(vectorLogManifold, variogram);
  }
}
