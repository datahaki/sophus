// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;

/** a tangent space without a specific base point
 * 
 * in the context of lie groups, the exponential maps between the Lie algebra g
 * and the Lie group G.
 * 
 * @see TangentSpace */
public interface Exponential {
  /** @param v vector in the tangent space
   * @return point in the manifold */
  Tensor exp(Tensor v);

  /** @return */
  default TensorUnaryOperator exp() {
    return this::exp;
  }

  /** @param q point in the manifold
   * @return vector in the tangent space */
  Tensor log(Tensor q);

  /** @return
   * @apiNote method exists for convenience */
  default TensorUnaryOperator log() {
    return this::log;
  }

  /** Example: in SO(n) the {@link #log()} returns square matrices that are
   * {@link AntisymmetricMatrixQ}. {@link #vectorLog()} returns only entries
   * above the diagonal as a vector.
   * 
   * @return operator that maps a point q to a tangent vectorized
   * typically without redundant entries */
  default TensorUnaryOperator vectorLog() {
    return q -> Flatten.of(log(q));
  }

  /** @return checks whether a given tensor is a tangent vector */
  ZeroDefectArrayQ isTangentQ();
}
