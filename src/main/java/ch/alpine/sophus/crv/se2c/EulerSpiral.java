// code by jph
package ch.alpine.sophus.crv.se2c;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Fresnel;
import ch.alpine.tensor.sca.Imag;
import ch.alpine.tensor.sca.Real;

/** maps arc length to elements in SE(2) covering group */
public enum EulerSpiral implements ScalarTensorFunction {
  FUNCTION;

  @Override
  public Tensor apply(Scalar t) {
    Scalar z = Fresnel.FUNCTION.apply(t);
    return Tensors.of( //
        Real.FUNCTION.apply(z), //
        Imag.FUNCTION.apply(z), //
        Pi.HALF.multiply(t).multiply(t));
  }
}
