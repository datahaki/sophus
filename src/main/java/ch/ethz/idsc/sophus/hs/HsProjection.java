// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

/** References:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020
 * 
 * "Projection Matrix" and
 * "Proofs involving the Moore-Penrose inverse"
 * on Wikipedia, 2020 */
public final class HsProjection implements Serializable {
  private final HsDesign hsDesign;

  /** @param vectorLogManifold non-null */
  public HsProjection(VectorLogManifold vectorLogManifold) {
    hsDesign = new HsDesign(vectorLogManifold);
  }

  /** projection matrix defines a projection of a tangent vector at given point to a vector in
   * the subspace of the tangent space at given point. The subspace depends on the given sequence.
   * 
   * <p>The projection to the subspace complement is defined by the matrix Id - projection
   * 
   * <p>In the literature the projection is referred to as influence matrix, or hat matrix.
   * 
   * @param sequence of length n
   * @param point
   * @return symmetric projection matrix of size n x n with eigenvalues either 1 or 0 */
  public Tensor influence(Tensor sequence, Tensor point) {
    Tensor levers = hsDesign.matrix(sequence, point);
    return levers.dot(PseudoInverse.of(levers));
  }

  /** projection matrix defines a projection of a tangent vector at given point to a vector in
   * the subspace of the tangent space at given point. The subspace depends on the given sequence.
   * 
   * <p>The projection to the subspace complement is defined by the matrix Id - projection
   * 
   * <p>In the literature the projection is referred to as residual marker matrix.
   * 
   * @param sequence of length n
   * @param point
   * @return symmetric projection matrix of size n x n with eigenvalues either 1 or 0 */
  public Tensor residualMarker(Tensor sequence, Tensor point) {
    AtomicInteger atomicInteger = new AtomicInteger();
    // I-X^+.X is projector on ker X
    return Tensor.of(influence(sequence, point).stream() //
        .map(Tensor::negate) // copy
        .map(row -> {
          row.set(RealScalar.ONE::add, atomicInteger.getAndIncrement());
          return row; // by ref
        }));
  }
}
