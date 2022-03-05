// code by jph
package ch.alpine.sophus.itp;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;

/** Inverse Distance Weighting does not reproduce linear functions in general. Therefore,
 * Inverse distance weights <b>do not</b> fall in the category of generalized barycentric
 * coordinates.
 * 
 * <p>Reference:
 * "A two-dimensional interpolation function for irregularly-spaced data"
 * by Donald Shepard, 1968 */
public record InverseDistanceWeighting( //
    ScalarUnaryOperator variogram, //
    TensorScalarFunction tensorScalarFunction) implements Genesis, Serializable {
  /** @param variogram
   * @return
   * @see InversePowerVariogram */
  public InverseDistanceWeighting(ScalarUnaryOperator variogram) {
    this(Objects.requireNonNull(variogram), Vector2Norm::of);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
        .map(tensorScalarFunction) //
        .map(variogram)));
  }
}
