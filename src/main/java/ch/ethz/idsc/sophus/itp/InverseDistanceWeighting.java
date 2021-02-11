// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.red.VectorNormInterface;

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
    return of(variogram, Norm._2);
  }

  /** @param variogram
   * @param vectorNormInterface
   * @return */
  public static Genesis of(ScalarUnaryOperator variogram, VectorNormInterface vectorNormInterface) {
    return new InverseDistanceWeighting( //
        Objects.requireNonNull(variogram), //
        Objects.requireNonNull(vectorNormInterface));
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;
  private final VectorNormInterface vectorNormInterface;

  private InverseDistanceWeighting(ScalarUnaryOperator variogram, VectorNormInterface vectorNormInterface) {
    this.variogram = variogram;
    this.vectorNormInterface = vectorNormInterface;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(levers.stream() //
        .map(vectorNormInterface::ofVector) //
        .map(variogram)));
  }
}
