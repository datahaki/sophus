// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.red.Times;

/** @see CirclePoints */
public enum EllipsePoints {
  ;
  /** @param n
   * @param scale vector of length 2
   * @return */
  public static Tensor of(int n, Tensor scale) {
    return Tensor.of(CirclePoints.of(n).stream().map(Times.operator(scale)));
  }

  /** @param n
   * @param width
   * @param height
   * @return */
  public static Tensor of(int n, Scalar width, Scalar height) {
    return of(n, Tensors.of(width, height));
  }
}
