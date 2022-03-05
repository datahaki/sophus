// code by jph
package ch.alpine.sophus.math;

import java.util.function.BinaryOperator;

import ch.alpine.tensor.Tensor;

public interface SeriesInterface extends BinaryOperator<Tensor> {
  /** function allows to investigate the rate of convergence
   * 
   * @param x
   * @param y
   * @return list of contributions up to given degree the sum of which is the
   * result of this binary operator */
  Tensor series(Tensor x, Tensor y);
}
