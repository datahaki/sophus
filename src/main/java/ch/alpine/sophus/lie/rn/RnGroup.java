// code by jph
package ch.alpine.sophus.lie.rn;

import java.util.Objects;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** Euclidean vector space, group action is addition, the neutral element is 0.
 * 
 * the implementation also covers the case R^1 where the elements are of type {@link Scalar}
 * (instead of a vector of length 1) */
public enum RnGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public RnGroupElement element(Tensor tensor) {
    return new RnGroupElement(Objects.requireNonNull(tensor));
  }

  @Override
  public Exponential exponential() {
    return RnExponential.INSTANCE;
  }

  /** geodesics in the Euclidean space R^n are straight lines */
  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor delta = q.subtract(p);
    return scalar -> p.add(delta.multiply(scalar));
  }
}
