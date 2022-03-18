// code by jph
package ch.alpine.sophus.hs.ad;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.Geodesic;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

public class HsAdGeodesic implements Geodesic, Serializable {
  private final HsAlgebra hsAlgebra;

  public HsAdGeodesic(HsAlgebra hsAlgebra) {
    this.hsAlgebra = Objects.requireNonNull(hsAlgebra);
  }

  @Override
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    return lambda -> split(p, q, lambda);
  }

  @Override
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    Tensor lift_p = hsAlgebra.lift(p);
    return hsAlgebra.action(lift_p, hsAlgebra.action(lift_p.negate(), q).multiply(scalar));
  }
}
