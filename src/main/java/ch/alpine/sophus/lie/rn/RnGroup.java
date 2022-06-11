// code by jph
package ch.alpine.sophus.lie.rn;

import java.util.Objects;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.sca.Chop;

/** Euclidean vector space, group action is addition, the neutral element is 0.
 * 
 * the implementation also covers the case R^1 where the elements are of type {@link Scalar}
 * (instead of a vector of length 1)
 * 
 * in Euclidean space
 * the exponential function is the identity
 * the logarithm function is the identity */
public enum RnGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public RnGroupElement element(Tensor tensor) {
    return new RnGroupElement(Objects.requireNonNull(tensor));
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    return v.copy();
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    return y.copy();
  }

  @Override // from Exponential
  public Tensor vectorLog(Tensor y) {
    return y.copy();
  }

  /** geodesics in the Euclidean space R^n are straight lines */
  @Override // from GeodesicSpace
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor delta = q.subtract(p);
    return scalar -> p.add(delta.multiply(scalar));
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return RnBiinvariantMean.INSTANCE;
  }
}
