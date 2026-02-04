// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.num.ReIm;
import ch.alpine.tensor.sca.erf.Fresnel;

/** maps arc length to elements in SE(2) covering group */
public enum EulerSpiral implements ScalarTensorFunction {
  FUNCTION;

  @Override
  public Tensor apply(Scalar t) {
    return ReIm.of(Fresnel.FUNCTION.apply(t)).vector().append(Pi.HALF.multiply(t).multiply(t));
  }
}
