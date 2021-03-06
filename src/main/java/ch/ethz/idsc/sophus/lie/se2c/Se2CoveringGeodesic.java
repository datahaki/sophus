// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.math.Geodesic;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;

public enum Se2CoveringGeodesic implements Geodesic {
  INSTANCE;

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Se2CoveringGroupElement p_act = Se2CoveringGroup.INSTANCE.element(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = Se2CoveringExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(Se2CoveringExponential.INSTANCE.exp(x.multiply(scalar)));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
