// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

/** row stochastic matrices */
public enum StochasticMatrixQ {
  ;
  /** @param tensor
   * @param chop
   * @return given tensor
   * @throws Exception if tensor is not a row-stochastic matrix */
  public static Tensor requireRows(Tensor tensor, Chop chop) {
    tensor.stream().forEach(vector -> AffineQ.require(vector, chop));
    return tensor;
  }
}
