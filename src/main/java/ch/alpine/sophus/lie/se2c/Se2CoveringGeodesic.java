// code by jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

public enum Se2CoveringGeodesic implements GeodesicSpace {
  INSTANCE;

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Se2CoveringGroupElement p_act = Se2CoveringGroup.INSTANCE.element(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = Se2CoveringExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(Se2CoveringExponential.INSTANCE.exp(x.multiply(scalar)));
  }
}
