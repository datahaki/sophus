// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.LeverageDistances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** anchor coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public class LeverageCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new LeverageCoordinate(vectorLogManifold, variogram);
  }

  /***************************************************/
  private final LeverageDistances leverageDistances;

  private LeverageCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    leverageDistances = new LeverageDistances(vectorLogManifold, variogram);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return leverageDistances.biinvariantVector(sequence, point).coordinate();
  }
}
