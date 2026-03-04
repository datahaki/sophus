// code by jph
package ch.alpine.sophus.api;

import java.util.List;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.VectorQ;

/** map between manifold M and tangent space T_p M for some point p in M
 * 
 * mapping from manifold to tangent space at predefined location x in manifold M
 * log_x : M -> TxM
 * operator returns a vector that satisfies {@link VectorQ}
 * this allows to concatenate log_x(p_i) for p_1, ..., p_n into a matrix */
public interface TangentSpace extends Exponential {
  /** @return base point of tangent space */
  Tensor basePoint();

  /** in all implemented examples, the array structure of the points of the manifold
   * is identical to the array structure of the vectors of the tangent space.
   * 
   * @return array structure of vectors */
  default List<Integer> dimensions() {
    return Dimensions.of(basePoint());
  }
}
