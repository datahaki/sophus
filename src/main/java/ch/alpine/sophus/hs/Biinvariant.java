// code by jph
package ch.alpine.sophus.hs;

import java.util.Objects;

import ch.alpine.sophus.gbc.LagrangeCoordinates;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public interface Biinvariant {
  /** @param manifold
   * @param sequence
   * @return operator that maps a point to a vector of relative distances to the elements in the given sequence */
  TensorUnaryOperator distances(Manifold manifold, Tensor sequence);

  /** @param manifold
   * @param variogram
   * @param sequence
   * @return distance vector with entries subject to given variogram */
  default TensorUnaryOperator var_dist(Manifold manifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = distances(manifold, sequence);
    Objects.requireNonNull(variogram);
    return point -> tensorUnaryOperator.apply(point).map(variogram);
  }

  /** @param manifold
   * @param variogram
   * @param sequence
   * @return distance vector with entries subject to given variogram normalized to sum up to 1 */
  default TensorUnaryOperator weighting(Manifold manifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = var_dist(manifold, variogram, sequence);
    return point -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point));
  }

  /** @param manifold
   * @param variogram
   * @param sequence
   * @return operator that provides barycentric coordinates */
  TensorUnaryOperator coordinate(Manifold manifold, ScalarUnaryOperator variogram, Tensor sequence);

  /** barycentric coordinate solution of Lagrange multiplier system
   * 
   * @param manifold
   * @param variogram
   * @param sequence
   * @return operator that provides barycentric coordinates */
  default TensorUnaryOperator lagrainate(Manifold manifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = weighting(manifold, variogram, sequence);
    return point -> LagrangeCoordinates.of( //
        new HsDesign(manifold).matrix(sequence, point), // TODO SOPHUS ALG levers are computed twice
        tensorUnaryOperator.apply(point)); // target
  }
}
