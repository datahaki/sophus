// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.Tensor;

/** @see ProjectedCoordinate */
@FunctionalInterface
public interface ProjectionInterface {
  /** projection matrix defines a projection of a tangent vector at given point to a vector in
   * the subspace of the tangent space at given point. The subspace depends on the given sequence.
   * 
   * <p>The projection to the subspace complement is defined by the matrix Id - projection
   * 
   * @param sequence of length n
   * @param point
   * @return symmetric projection matrix of size n x n with eigenvalues either 1 or 0 */
  Tensor projection(Tensor sequence, Tensor point);
}
