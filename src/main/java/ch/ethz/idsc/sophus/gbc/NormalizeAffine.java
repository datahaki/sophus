// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;

/** functionality makes strict assumptions on input parameters
 * therefore the implementation is restricted to package visibility */
/* package */ enum NormalizeAffine {
  ;
  /** @param vector
   * @param nullsp orthogonal matrix
   * @return
   * @see OrthogonalMatrixQ */
  public static Tensor fromNullspace(Tensor vector, Tensor nullsp) {
    return of(product(vector, nullsp));
  }

  /** VISIBILITY ONLY FOR TESTING
   * 
   * for the orthogonal matrix nullsp, the relation hold:
   * Transpose[nullsp] == PseudoInverse[nullsp]
   * 
   * @param vector
   * @param nullsp orthogonal matrix
   * @return vector . Transpose[nullsp] . nullsp
   * @see OrthogonalMatrixQ */
  /* package */ static Tensor product(Tensor vector, Tensor nullsp) {
    return nullsp.dot(vector).dot(nullsp);
  }

  private static Tensor of(Tensor weights) {
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}
