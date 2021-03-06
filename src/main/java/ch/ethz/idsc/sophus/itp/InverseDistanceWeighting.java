// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

/** Inverse Distance Weighting does not reproduce linear functions in general. Therefore,
 * Inverse distance weights <b>do not</b> fall in the category of generalized barycentric
 * coordinates.
 * 
 * <p>Reference:
 * "A two-dimensional interpolation function for irregularly-spaced data"
 * by Donald Shepard, 1968 */
public class InverseDistanceWeighting implements Genesis, Serializable {
  /** @param variogram
   * @return
   * @see InversePowerVariogram */
  public static Genesis of(ScalarUnaryOperator variogram) {
    return of(variogram, Vector2Norm::of);
  }

  /** @param variogram
   * @param tensorScalarFunction norm
   * @return */
  public static Genesis of(ScalarUnaryOperator variogram, TensorScalarFunction tensorScalarFunction) {
    return new InverseDistanceWeighting( //
        Objects.requireNonNull(variogram), //
        Objects.requireNonNull(tensorScalarFunction));
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;
  private final TensorScalarFunction tensorScalarFunction;

  private InverseDistanceWeighting(ScalarUnaryOperator variogram, TensorScalarFunction tensorScalarFunction) {
    this.variogram = variogram;
    this.tensorScalarFunction = tensorScalarFunction;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
        .map(tensorScalarFunction) //
        .map(variogram)));
  }
}
