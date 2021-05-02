// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;

/** mapping from manifold to tangent space at predefined location x in manifold M
 * log_x : M -> TxM
 * operator returns a vector that satisfies {@link VectorQ}
 * this allows to concatenate log_x(p_i) for p_1, ..., p_n into a matrix */
@FunctionalInterface
public interface TangentSpace {
  /** @param point on manifold
   * @return vector in tangent space that satisfies Exp_x(vector) == point */
  Tensor vectorLog(Tensor point);
}
