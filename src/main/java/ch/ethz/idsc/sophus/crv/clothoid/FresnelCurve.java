// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.sca.Fresnel;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;

public enum FresnelCurve implements ScalarTensorFunction {
  FUNCTION;

  @Override
  public Tensor apply(Scalar x) {
    Scalar z = Fresnel.FUNCTION.apply(x);
    return Tensors.of( //
        Real.FUNCTION.apply(z), //
        Imag.FUNCTION.apply(z));
  }
}
