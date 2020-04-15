// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.red.Total;

/** functionality makes strict assumptions on input parameters
 * therefore the implementation is restricted to package visibility */
/* package */ enum NormalizeAffine {
  ;
  /** @param target
   * @param projection symmetric square matrix enforces linear reproduction
   * @return */
  public static Tensor fromProjection(Tensor target, Tensor projection) {
    return of(target.dot(projection));
  }

  /** @param vector
   * @param nullsp orthogonal matrix
   * @return
   * @see OrthogonalMatrixQ */
  public static Tensor fromNullspace(Tensor vector, Tensor nullsp) {
    return of(product(vector, nullsp));
  }

  /** VISIBILITY ONLY FOR TESTING
   * 
   * @param vector
   * @param nullsp orthogonal matrix
   * @return vector . PseudoInverse[nullsp] . nullsp
   * @see OrthogonalMatrixQ */
  /* package */ static Tensor product(Tensor vector, Tensor nullsp) {
    // return LeastSquares.usingSvd(Transpose.of(nullsp), vector).dot(nullsp);
    // return vector.dot(Transpose.of(nullsp)).dot(nullsp);
    return nullsp.dot(vector).dot(nullsp);
  }

  private static Tensor of(Tensor weights) {
    Scalar total = Total.ofVector(weights);
    return Tolerance.CHOP.allZero(total) //
        ? UnitVector.of(weights.length(), ArgMax.of(weights))
        : weights.divide(total);
  }
}
