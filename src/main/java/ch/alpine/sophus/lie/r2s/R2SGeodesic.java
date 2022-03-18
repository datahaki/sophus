// code by jph
package ch.alpine.sophus.lie.r2s;

import ch.alpine.sophus.api.Geodesic;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

public enum R2SGeodesic implements Geodesic {
  INSTANCE;

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    R2SGroupElement p_act = new R2SGroupElement(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = R2SExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(R2SExponential.INSTANCE.exp(x.multiply(scalar)));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
