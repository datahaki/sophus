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
import ch.ethz.idsc.tensor.nrm.VectorNorm2;

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
    return of(variogram, VectorNorm2::of);
  }

  /** @param variogram
   * @param vectorNormInterface
   * @return */
  public static Genesis of(ScalarUnaryOperator variogram, TensorScalarFunction vectorNormInterface) {
    return new InverseDistanceWeighting( //
        Objects.requireNonNull(variogram), //
        Objects.requireNonNull(vectorNormInterface));
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;
  private final TensorScalarFunction tensorScalarFunction;

  private InverseDistanceWeighting(ScalarUnaryOperator variogram, TensorScalarFunction vectorNormInterface) {
    this.variogram = variogram;
    this.tensorScalarFunction = vectorNormInterface;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
        .map(tensorScalarFunction) //
        .map(variogram)));
  }
}
