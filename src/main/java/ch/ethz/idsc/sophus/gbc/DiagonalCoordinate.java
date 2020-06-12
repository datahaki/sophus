// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.DiagonalDistances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** relative coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public class DiagonalCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new DiagonalCoordinate(vectorLogManifold, variogram);
  }

  /***************************************************/
  private final DiagonalDistances relative1Distances;

  private DiagonalCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    relative1Distances = new DiagonalDistances(vectorLogManifold, variogram);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return relative1Distances.biinvariantVector(sequence, point).coordinate();
  }
}
