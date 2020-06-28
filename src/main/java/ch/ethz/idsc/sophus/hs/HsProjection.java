// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

/** Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public final class HsProjection implements Serializable {
  private final HsLevers hsLevers;

  /** @param vectorLogManifold non-null */
  public HsProjection(VectorLogManifold vectorLogManifold) {
    hsLevers = new HsLevers(vectorLogManifold);
  }

  /** projection matrix defines a projection of a tangent vector at given point to a vector in
   * the subspace of the tangent space at given point. The subspace depends on the given sequence.
   * 
   * <p>The projection to the subspace complement is defined by the matrix Id - projection
   * 
   * @param sequence of length n
   * @param point
   * @return symmetric projection matrix of size n x n with eigenvalues either 1 or 0 */
  public Tensor projection(Tensor sequence, Tensor point) {
    Tensor levers = hsLevers.levers(sequence, point);
    return IdentityMatrix.of(sequence.length()).subtract(levers.dot(PseudoInverse.of(levers)));
    // ---
    // alternative implementation:
    // Tensor nullsp = LeftNullSpace.usingQR(levers);
    // return Transpose.of(nullsp).dot(nullsp);
  }
}
