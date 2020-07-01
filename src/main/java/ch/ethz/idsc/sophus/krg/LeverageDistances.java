// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum LeverageDistances {
  ;
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static WeightingInterface fast(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new TargetDistances(vectorLogManifold, Objects.requireNonNull(variogram));
  }

  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static WeightingInterface slow(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new AnchorDistances(vectorLogManifold, Objects.requireNonNull(variogram));
  }
}
