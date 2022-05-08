// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** geodesics in the Euclidean space R^n are straight lines */
public enum RnGeodesic implements GeodesicSpace {
  INSTANCE;

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor delta = q.subtract(p);
    return scalar -> p.add(delta.multiply(scalar));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
