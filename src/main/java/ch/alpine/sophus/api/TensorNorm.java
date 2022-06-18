// code by jph
package ch.alpine.sophus.api;

import java.io.Serializable;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** Norm for vector space
 * 
 * Source: Wikipedia */
@FunctionalInterface
public interface TensorNorm extends Serializable {
  /** For u, v from vector space, the norm satisfies
   * 1) p(u + v) <= p(u) + p(v) (being subadditive or satisfying the triangle inequality).
   * 2) p(av) = |a| p(v) (being absolutely homogeneous or absolutely scalable).
   * 3) If p(v) = 0 then v = 0 is the zero vector (being positive definite or being point-separating).
   * 
   * @param tensor
   * @return */
  Scalar norm(Tensor tensor);
}
