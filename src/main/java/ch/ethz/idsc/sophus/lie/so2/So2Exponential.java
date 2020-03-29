// code by ob
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** a group element SO(2) is represented as a Scalar in [-pi, pi)
 * 
 * an element of the algebra so(2) is represented as 'vector' of length 1
 * (Actually a scalar, but Exponential requires a vector) */
public enum So2Exponential implements Exponential {
  INSTANCE;

  @Override // from Exponential
  public Scalar exp(Tensor scalar) {
    return (Scalar) scalar;
  }

  @Override // from Exponential
  public Scalar log(Tensor scalar) {
    return (Scalar) scalar;
  }
}
