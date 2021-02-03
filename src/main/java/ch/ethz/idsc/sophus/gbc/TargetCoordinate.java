// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Mahalanobis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;

/** target coordinate is the preferred way to evaluate
 * inverse leverage coordinates.
 * 
 * the slower alternative is {@link AnchorCoordinate}.
 * 
 * <p>Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see LeverageCoordinate */
public class TargetCoordinate implements Genesis, Serializable {
  /** @param variogram
   * @return */
  public static Genesis of(ScalarUnaryOperator variogram) {
    return new TargetCoordinate(Objects.requireNonNull(variogram));
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;

  private TargetCoordinate(ScalarUnaryOperator variogram) {
    this.variogram = variogram;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Mahalanobis mahalanobis = new Mahalanobis(levers);
    return StaticHelper.barycentric( //
        mahalanobis.design(), //
        NormalizeTotal.FUNCTION.apply(mahalanobis.leverages_sqrt().map(variogram)));
  }
}
