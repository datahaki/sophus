// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.sophus.krg.Mahalanobis;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.red.Diagonal;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** References:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020
 * 
 * "Projection Matrix" and
 * "Proofs involving the Moore-Penrose inverse"
 * on Wikipedia, 2020 */
public final class HsInfluence implements Serializable {
  private final Tensor matrix;

  /** @param tangentSpace
   * @param sequence */
  public HsInfluence(TangentSpace tangentSpace, Tensor sequence) {
    Tensor design = Tensor.of(sequence.stream().map(tangentSpace::vectorLog)); // HsDesign matrix
    matrix = design.dot(PseudoInverse.of(design)); // textbook formula
  }

  /** projection matrix defines a projection of a tangent vector at given point to a vector in
   * the subspace of the tangent space at given point. The subspace depends on the given sequence.
   * 
   * <p>The projection to the subspace complement is defined by the matrix Id - projection
   * 
   * <p>In the literature the projection is referred to as influence matrix, hat matrix. or
   * predicted value maker matrix.
   * 
   * @return symmetric projection matrix of size n x n with eigenvalues either 1 or 0 */
  public Tensor matrix() {
    return matrix.unmodifiable();
  }

  /** @return diagonal entries of influence matrix guaranteed to be in the unit interval [0, 1] */
  public Tensor leverages() {
    // theory guarantees that entries of diagonal are in interval [0, 1]
    // but the numerics don't always reflect that.
    return Diagonal.of(matrix).map(Clips.unit());
  }

  /** @return sqrt of leverages identical to {@link Mahalanobis} distance */
  public Tensor leverages_sqrt() {
    return leverages().map(Sqrt.FUNCTION);
  }

  /** projection matrix defines a projection of a tangent vector at given point to a vector in
   * the subspace of the tangent space at given point. The subspace depends on the given sequence.
   * 
   * <p>The projection to the subspace complement is defined by the matrix Id - projection
   * 
   * <p>In the literature the projection is referred to as residual maker matrix.
   * 
   * @return symmetric projection matrix of size n x n with eigenvalues either 1 or 0 */
  public Tensor residualMaker() {
    AtomicInteger atomicInteger = new AtomicInteger();
    // I-X^+.X is projector on ker X
    return Tensor.of(matrix.stream() //
        .map(Tensor::negate) // copy
        .map(row -> {
          row.set(RealScalar.ONE::add, atomicInteger.getAndIncrement());
          return row; // by ref
        }));
  }
}
