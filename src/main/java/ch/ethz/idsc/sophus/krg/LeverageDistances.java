// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** leverage distances are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public enum LeverageDistances {
  ;
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static WeightingInterface of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new TargetDistances(vectorLogManifold, Objects.requireNonNull(variogram));
  }
}
