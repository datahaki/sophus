// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** in Euclidean space
 * the exponential function is the identity
 * the logarithm function is the identity */
public enum RnExponential implements Exponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor x) {
    return x.copy();
  }

  @Override // from Exponential
  public Tensor log(Tensor g) {
    return g.copy();
  }
}
