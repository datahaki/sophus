// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

import ch.alpine.sophus.math.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

public record HsAdGeodesic(HsAlgebra hsAlgebra) implements GeodesicSpace, Serializable {
  @Override
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor lift_p = hsAlgebra.lift(p);
    Tensor action = hsAlgebra.action(lift_p.negate(), q);
    return scalar -> hsAlgebra.action(lift_p, action.multiply(scalar));
  }
}
