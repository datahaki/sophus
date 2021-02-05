// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.dv.LeveragesDistanceVector;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.mat.InfluenceMatrix;
import ch.ethz.idsc.tensor.mat.Mahalanobis;

/** target coordinate is the preferred way to evaluate
 * inverse leverage coordinates.
 * 
 * <p>References:
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020
 * 
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see LeveragesDistanceVector
 * @see LeveragesCoordinate */
public class LeveragesGenesis implements Genesis, Serializable {
  /** @param variogram
   * @return */
  public static Genesis of(ScalarUnaryOperator variogram) {
    return new LeveragesGenesis(Objects.requireNonNull(variogram));
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;

  private LeveragesGenesis(ScalarUnaryOperator variogram) {
    this.variogram = variogram;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    // Mahalanobis is faster than InfluenceMatrix[...] for this computation
    InfluenceMatrix influenceMatrix = new Mahalanobis(levers);
    Tensor vector = NormalizeTotal.FUNCTION.apply(influenceMatrix.leverages_sqrt().map(variogram));
    return NormalizeTotal.FUNCTION.apply(influenceMatrix.kernel(vector));
  }
}
