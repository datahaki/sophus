// code by jph
package ch.alpine.sophus.hs.ad;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

public class HsAdGeodesic implements GeodesicSpace, Serializable {
  private final HsAlgebra hsAlgebra;

  public HsAdGeodesic(HsAlgebra hsAlgebra) {
    this.hsAlgebra = Objects.requireNonNull(hsAlgebra);
  }

  @Override
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor lift_p = hsAlgebra.lift(p);
    Tensor action = hsAlgebra.action(lift_p.negate(), q);
    return scalar -> hsAlgebra.action(lift_p, action.multiply(scalar));
  }
}
