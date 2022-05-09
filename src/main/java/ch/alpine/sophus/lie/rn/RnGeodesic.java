// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** geodesics in the Euclidean space R^n are straight lines */
public enum RnGeodesic implements GeodesicSpace {
  INSTANCE;
  // TODO check repo for Tensors.of vs. Unprotect.byRef

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor delta = q.subtract(p);
    return scalar -> p.add(delta.multiply(scalar));
  }
}
