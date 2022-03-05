// code by jph
package ch.alpine.sophus.math;

import java.util.Optional;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

/** The algorithm is also known as the <em>Von Mises iteration</em>.
 * 
 * https://en.wikipedia.org/wiki/Power_iteration */
// TODO SOPHUS API not used at all
public enum PowerIteration {
  ;
  /** max iterations for each dimension */
  private static final int FACTOR = 15;
  private static final Chop CHOP = Chop._15;

  /** @param matrix square
   * @return Eigenvector to the largest eigenvalue (with high probability) */
  public static Optional<Tensor> of(Tensor matrix) {
    return of(matrix, RandomVariate.of(NormalDistribution.standard(), matrix.length()));
  }

  /** @param matrix square
   * @param vector seed
   * @return Eigenvector to the largest eigenvalue normalized to unit length */
  public static Optional<Tensor> of(Tensor matrix, Tensor vector) {
    int max = matrix.length() * FACTOR;
    for (int iteration = 0; iteration < max; ++iteration) {
      final Tensor prev = vector;
      vector = Vector2Norm.NORMALIZE.apply(matrix.dot(vector));
      if (CHOP.allZero(prev.subtract(vector)) || //
          CHOP.allZero(prev.add(vector)))
        return Optional.of(vector);
    }
    return Optional.empty();
  }
}
