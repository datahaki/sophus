// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.red.Times;

public enum CogPoints {
  ;
  /** the first coordinate is always {1, 0}.
   * the orientation of the points is counter-clockwise.
   * 
   * @param n
   * @param hi
   * @param lo
   * @return (n * 4) x 2 matrix */
  public static Tensor of(int n, Scalar hi, Scalar lo) {
    return Times.of(Flatten.of(ConstantArray.of(Tensors.of(hi, hi, lo, lo), n)), CirclePoints.of(n * 4));
  }
}
