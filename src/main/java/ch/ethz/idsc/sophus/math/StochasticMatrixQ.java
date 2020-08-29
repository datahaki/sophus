// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

/** row stochastic matrices */
public enum StochasticMatrixQ {
  ;
  /** @param tensor
   * @return given tensor
   * @throws Exception if tensor is not a row-stochastic matrix */
  public static Tensor requireRows(Tensor tensor, Chop chop) {
    tensor.stream().forEach(vector -> AffineQ.require(vector, chop));
    return tensor;
  }

  /** @param tensor
   * @return given tensor
   * @throws Exception if tensor is not a row-stochastic matrix */
  public static Tensor requireRows(Tensor tensor) {
    tensor.stream().forEach(AffineQ::require);
    return tensor;
  }
}
