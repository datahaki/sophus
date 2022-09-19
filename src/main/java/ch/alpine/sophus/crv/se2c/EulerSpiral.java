// code by jph
package ch.alpine.sophus.crv.se2c;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Im;
import ch.alpine.tensor.sca.Re;
import ch.alpine.tensor.sca.erf.Fresnel;

/** maps arc length to elements in SE(2) covering group */
public enum EulerSpiral implements ScalarTensorFunction {
  FUNCTION;

  @Override
  public Tensor apply(Scalar t) {
    Scalar z = Fresnel.FUNCTION.apply(t);
    return Tensors.of( //
        Re.FUNCTION.apply(z), //
        Im.FUNCTION.apply(z), //
        Pi.HALF.multiply(t).multiply(t));
  }
}
