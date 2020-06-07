// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Relative1Distances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** relative coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public class Relative1Coordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new Relative1Coordinate(vectorLogManifold, variogram);
  }

  /***************************************************/
  private final Relative1Distances relative1Distances;

  private Relative1Coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    relative1Distances = new Relative1Distances(vectorLogManifold, variogram);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return relative1Distances.biinvariantVector(sequence, point).coordinate();
  }
}
