// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

public enum IdempotentQ {
  ;
  /** @param matrix
   * @param chop
   * @return */
  public static boolean of(Tensor matrix, Chop chop) {
    return chop.isClose(matrix, matrix.dot(matrix));
  }
}
