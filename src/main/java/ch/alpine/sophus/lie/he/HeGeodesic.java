// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

public enum HeGeodesic implements GeodesicSpace {
  INSTANCE;

  @Override // from Geodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    HeGroupElement p_act = new HeGroupElement(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = HeExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(HeExponential.INSTANCE.exp(x.multiply(scalar)));
  }
}
