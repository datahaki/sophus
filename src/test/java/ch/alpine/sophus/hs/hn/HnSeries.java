// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.Tensors;

public enum HnSeries {
  ;
  private static final int MAX_ITERATIONS = 128;

  /** @param x
   * @return {Cosh[sqrt(x)], Sinh[sqrt(x)]/sqrt(x)} */
  public static Tensor of(Scalar x) {
    Scalar c = x.zero();
    Scalar s = x.zero();
    Scalar xk = x.one();
    int index = 0;
    for (int k = 0; k <= MAX_ITERATIONS; ++k) {
      c = c.add(xk);
      xk = xk.divide(DoubleScalar.of(++index));
      s = s.add(xk);
      xk = xk.divide(DoubleScalar.of(++index));
      xk = xk.multiply(x);
      if (c.equals(c.add(xk)))
        return Tensors.of(c, s);
    }
    throw TensorRuntimeException.of(x);
  }
}
