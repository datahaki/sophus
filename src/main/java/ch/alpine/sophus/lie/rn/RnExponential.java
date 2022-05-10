// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.tensor.Tensor;

/** in Euclidean space
 * the exponential function is the identity
 * the logarithm function is the identity */
/* package */ enum RnExponential implements Exponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    return v.copy();
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    return y.copy();
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return y.copy();
  }
}
