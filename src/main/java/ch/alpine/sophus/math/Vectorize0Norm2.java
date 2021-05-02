// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Sqrt;

public enum Vectorize0Norm2 implements TensorScalarFunction {
  INSTANCE;

  private static final Scalar SQRT2 = Sqrt.FUNCTION.apply(RealScalar.TWO);

  @Override
  public Scalar apply(Tensor vector) {
    int n = vector.length();
    double kd = (Math.sqrt(1 + 8 * n) - 1) / 2;
    int k = (int) kd;
    if (1e-10 < Math.abs(kd - k))
      throw TensorRuntimeException.of(vector);
    Tensor vn = Tensors.reserve(vector.length());
    int index = 0;
    int next = 0;
    int delta = 0;
    for (Tensor value : vector) {
      if (index == next)
        next += ++delta;
      vn.append(++index == next ? value : value.multiply(SQRT2));
    }
    return Vector2Norm.of(vn);
  }
}
