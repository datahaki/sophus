// code by jph
package ch.alpine.sophus.lie.su;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.pow.Sqrt;

public enum Su3AlgebraBasis {
  ;
  private static final Scalar SQRT3 = Sqrt.FUNCTION.apply(Rational.THIRD);

  public static Tensor basis() {
    // https://en.wikipedia.org/wiki/Gell-Mann_matrices
    Tensor u0 = Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}");
    Tensor u1 = Tensors.fromString("{{0, -I, 0}, {I, 0, 0}, {0, 0, 0}}");
    Tensor u2 = Tensors.fromString("{{1, 0, 0}, {0, -1, 0}, {0, 0, 0}}");
    Tensor u3 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}");
    Tensor u4 = Tensors.fromString("{{0, 0, -I}, {0, 0, 0}, {I, 0, 0}}");
    Tensor u5 = Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}}");
    Tensor u6 = Tensors.fromString("{{0, 0, 0}, {0, 0, -I}, {0, I, 0}}");
    Tensor u7 = Tensors.fromString("{{1, 0, 0}, {0, 1, 0}, {0, 0, -2}}").multiply(SQRT3);
    return Tensors.of(u0, u1, u2, u3, u4, u5, u6, u7).multiply(Rational.HALF);
  }
}
