// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** list of biinvariant weightings and barycentric coordinates regardless whether a
 * biinvariant metric exists on the manifold.
 * 
 * <p>Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public interface Biinvariant {
  HsDesign hsDesign();

  /** @param sequence
   * @return operator that maps a point to a vector of relative distances to the elements in the given sequence */
  TensorUnaryOperator distances(Tensor sequence);

  /** @param variogram
   * @param sequence
   * @return distance vector with entries subject to given variogram */
  TensorUnaryOperator var_dist(ScalarUnaryOperator variogram, Tensor sequence);

  /** @param variogram
   * @param sequence
   * @return distance vector with entries subject to given variogram normalized to sum up to 1 */
  TensorUnaryOperator weighting(ScalarUnaryOperator variogram, Tensor sequence);

  /** @param variogram
   * @param sequence
   * @return operator that provides barycentric coordinates */
  TensorUnaryOperator coordinate(ScalarUnaryOperator variogram, Tensor sequence);

  /** barycentric coordinate solution of Lagrange multiplier system
   * 
   * 
   * @param variogram
   * @param sequence
   * @return operator that provides barycentric coordinates */
  TensorUnaryOperator lagrainate(ScalarUnaryOperator variogram, Tensor sequence);
}
