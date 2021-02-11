// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public interface Biinvariant {
  /** @param vectorLogManifold
   * @param sequence
   * @return operator that maps a point to a vector of relative distances to the elements in the given sequence */
  TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence);

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return distance vector with entries subject to given variogram */
  TensorUnaryOperator var_dist(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence);

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return distance vector with entries subject to given variogram normalized to sum up to 1 */
  TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence);

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return operator that provides barycentric coordinates */
  TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence);

  /** barycentric coordinate solution of lagrange multiplier system
   * 
   * @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return operator that provides barycentric coordinates */
  TensorUnaryOperator lagrainate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence);
}
