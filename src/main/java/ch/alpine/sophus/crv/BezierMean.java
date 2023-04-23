// code by jph
package ch.alpine.sophus.crv;

import java.util.Objects;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.itp.BernsteinBasis;

public enum BezierMean {
  ;
  /** evaluation using biinvariantMean with weights from Bernstein polynomials
   * 
   * @param biinvariantMean
   * @param sequence non-empty tensor
   * @return
   * @see BernsteinBasis */
  public static ScalarTensorFunction of(BiinvariantMean biinvariantMean, Tensor sequence) {
    Objects.requireNonNull(biinvariantMean);
    int degree = Integers.requirePositiveOrZero(sequence.length() - 1);
    return scalar -> biinvariantMean.mean(sequence, BernsteinBasis.of(degree, scalar));
  }
}
