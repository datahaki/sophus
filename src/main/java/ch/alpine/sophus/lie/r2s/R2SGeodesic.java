// code by jph
package ch.alpine.sophus.lie.r2s;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

public enum R2SGeodesic implements GeodesicSpace {
  INSTANCE;

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    R2SGroupElement p_act = new R2SGroupElement(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = R2SExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(R2SExponential.INSTANCE.exp(x.multiply(scalar)));
  }
}
