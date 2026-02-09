// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.red.Diagonal;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.pow.Sqrt;

/** Reference:
 * geomstats */
public enum FullRankCorrelationMatrix {
  ;
  /** "from_covariance"
   * 
   * @param matrix symmetric positive definite
   * @return */
  public static Tensor fromSpd(Tensor matrix) {
    Tensor diag = Diagonal.of(matrix).maps(Scalar::reciprocal).maps(Sqrt.FUNCTION);
    return Times.of(matrix, TensorProduct.of(diag, diag));
  }
}
