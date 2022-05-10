// code by ob
package ch.alpine.sophus.lie.dt;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/* package */ enum DtGeodesic implements GeodesicSpace {
  INSTANCE;

  @Override // from Geodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    DtGroupElement p_act = DtGroup.INSTANCE.element(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = DtExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(DtExponential.INSTANCE.exp(x.multiply(scalar)));
  }
}
