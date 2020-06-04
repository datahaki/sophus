// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** relative coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public final class Relative0Coordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new Relative0Coordinate(vectorLogManifold, InverseDiagonal.of(variogram));
  }

  /***************************************************/
  private final HsProjection hsProjection;
  private final TensorUnaryOperator target;

  private Relative0Coordinate(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    hsProjection = new HsProjection(vectorLogManifold);
    this.target = target;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor projection = hsProjection.projection(sequence, point);
    return NormalizeAffine.fromProjection(target.apply(projection), projection);
  }
}
