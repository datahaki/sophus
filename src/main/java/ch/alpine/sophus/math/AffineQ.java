// code by ob, jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

/** check if entries add up to one */
public enum AffineQ {
  ;
  /** @param weights vector
   * @param chop
   * @return given vector
   * @throws Exception if scalar entries of given mask do not add up to one */
  public static Tensor require(Tensor weights, Chop chop) {
    chop.requireClose(Total.of(weights), weights.Get(0).one());
    return weights;
  }

  /** @param weights vector
   * @return given vector
   * @throws Exception if scalar entries of given mask do not add up to one */
  public static Tensor require(Tensor weights) {
    return require(weights, Tolerance.CHOP);
  }
}
