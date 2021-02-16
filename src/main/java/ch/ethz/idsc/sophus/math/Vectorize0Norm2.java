// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;
import ch.ethz.idsc.tensor.sca.Sqrt;

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
    int len = 0;
    int next = 0;
    for (int i = 0; i < n; ++i) {
      if (i == next)
        next += ++len;
      Scalar value = vector.Get(i);
      vn.append(i + 1 == next //
          ? value
          : value.multiply(SQRT2));
    }
    return VectorNorm2.of(vn);
  }
}
