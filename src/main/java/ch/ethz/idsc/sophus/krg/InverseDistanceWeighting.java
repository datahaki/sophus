// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Inverse Distance Weighting does not reproduce linear functions in general. Therefore,
 * Inverse distance weights <b>do not</b> fall in the category of generalized barycentric
 * coordinates.
 * 
 * <p>Reference:
 * "A two-dimensional interpolation function for irregularly-spaced data"
 * by Donald Shepard, 1968 */
public class InverseDistanceWeighting implements Genesis, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static Genesis of(ScalarUnaryOperator variogram) {
    return new InverseDistanceWeighting(Objects.requireNonNull(variogram));
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;

  private InverseDistanceWeighting(ScalarUnaryOperator variogram) {
    this.variogram = variogram;
  }

  @Override
  public Tensor origin(Tensor levers) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
        .map(Norm._2::ofVector) //
        .map(variogram)));
  }
}
