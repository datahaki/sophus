// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;

/** map between manifold M and tangent space T_p M for some point p in M
 * 
 * mapping from manifold to tangent space at predefined location x in manifold M
 * log_x : M -> TxM
 * operator returns a vector that satisfies {@link VectorQ}
 * this allows to concatenate log_x(p_i) for p_1, ..., p_n into a matrix */
public interface Exponential {
  /** exponential map is a map from the Lie algebra g of a Lie group G to the group
   * 
   * @param v vector in the tangent space
   * @return point in the manifold */
  Tensor exp(Tensor v);

  /** @param q point in the manifold
   * @return vector in the tangent space */
  Tensor log(Tensor q);

  /** @return
   * @apiNote method exists for convenience */
  default TensorUnaryOperator log() {
    return this::log;
  }

  /** @return checks whether a given tensor is a tangent vector */
  ZeroDefectArrayQ isTangentQ();
}
