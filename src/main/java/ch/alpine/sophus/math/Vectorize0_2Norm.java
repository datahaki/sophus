// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Sqrt;

public enum Vectorize0_2Norm implements TensorNorm {
  INSTANCE;

  private static final Scalar SQRT2 = Sqrt.FUNCTION.apply(RealScalar.TWO);

  /** @param vector has to be a triangle number */
  @Override
  public Scalar norm(Tensor vector) {
    int n = vector.length();
    // TODO use mathematica to simplify!?
    Scalar kd = Sqrt.FUNCTION.apply(RealScalar.of(1 + 8 * n)).subtract(RealScalar.ONE).multiply(RationalScalar.HALF);
    ExactScalarQ.require(kd);
    Tensor vn = Tensors.reserve(vector.length());
    int index = 0;
    int next = 0;
    int skip = 0;
    for (Tensor value : vector) {
      if (index == next)
        next += ++skip;
      vn.append(++index == next ? value : value.multiply(SQRT2));
    }
    return Vector2Norm.of(vn);
  }
}
