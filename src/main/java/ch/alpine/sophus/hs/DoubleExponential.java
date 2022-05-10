// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** Reference:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020, p. 4 */
public record DoubleExponential(HomogeneousSpace hsManifold) implements Serializable {
  public DoubleExponential {
    Objects.requireNonNull(hsManifold);
  }

  public DoubleExponentialPoint at(Tensor x) {
    return new DoubleExponentialPoint(x);
  }

  public class DoubleExponentialPoint implements Serializable {
    private final Tensor x;
    private final Exponential exponential;

    private DoubleExponentialPoint(Tensor x) {
      this.x = x;
      exponential = hsManifold.exponential(x);
    }

    public TensorUnaryOperator operator(Tensor v) {
      Tensor xv = exponential.exp(v);
      Exponential exp_xv = hsManifold.exponential(xv);
      TensorUnaryOperator operator = hsManifold.hsTransport().shift(x, xv);
      return w -> exp_xv.exp(operator.apply(w));
    }
  }
}
