// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.LeastSquares;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.red.Total;

/* package */ enum NormalizeAffine {
  ;
  /** @param target
   * @param projection symmetric square matrix enforces linear reproduction
   * @return */
  public static Tensor fromProjection(Tensor target, Tensor projection) {
    return of(target.dot(projection));
  }

  /** @param vector
   * @param nullsp
   * @return */
  public static Tensor fromNullspace(Tensor vector, Tensor nullsp) {
    return of(product(vector, nullsp));
  }

  /** VISIBILITY ONLY FOR TESTING
   * 
   * @param vector
   * @param nullsp
   * @return vector . PseudoInverse[nullsp] . nullsp */
  /* package */ static Tensor product(Tensor vector, Tensor nullsp) {
    return LeastSquares.usingSvd(Transpose.of(nullsp), vector).dot(nullsp);
  }

  private static Tensor of(Tensor weights) {
    Scalar total = Total.ofVector(weights);
    return Tolerance.CHOP.allZero(total) //
        ? UnitVector.of(weights.length(), ArgMax.of(weights))
        : weights.divide(total);
  }
}
