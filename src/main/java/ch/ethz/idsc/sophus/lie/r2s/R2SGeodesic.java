// code by jph
package ch.ethz.idsc.sophus.lie.r2s;

import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;

public enum R2SGeodesic implements GeodesicInterface {
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
