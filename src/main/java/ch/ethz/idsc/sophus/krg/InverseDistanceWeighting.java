// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
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
public class InverseDistanceWeighting implements WeightingInterface, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static WeightingInterface of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new InverseDistanceWeighting(vectorLogManifold, Objects.requireNonNull(variogram));
  }

  /***************************************************/
  private final HsDesign hsDesign;
  private final ScalarUnaryOperator variogram;

  private InverseDistanceWeighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.variogram = variogram;
  }

  @Override
  public Tensor weights(Tensor sequence, Tensor point) {
    return NormalizeTotal.FUNCTION.apply(Tensor.of(hsDesign.matrix(sequence, point).stream() //
        .map(Norm._2::ofVector) //
        .map(variogram)));
  }
}
