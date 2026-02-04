// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

/** row stochastic matrices */
// TODO SOPHUS only used for testing
public enum StochasticMatrixQ {
  ;
  /** @param tensor
   * @param chop
   * @return given tensor
   * @throws Exception if tensor is not a row-stochastic matrix */
  public static Tensor requireRows(Tensor tensor, Chop chop) {
    tensor.forEach(vector -> AffineQ.require(vector, chop));
    return tensor;
  }
}
