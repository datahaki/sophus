// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** in Euclidean space
 * the exponential function is the identity
 * the logarithm function is the identity
 * 
 * @see LieExponential */
public enum RnExponential implements Exponential {
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
