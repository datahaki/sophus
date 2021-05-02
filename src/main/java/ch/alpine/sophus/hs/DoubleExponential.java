// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** Reference:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020, p. 4 */
public class DoubleExponential implements Serializable {
  private final HsManifold hsManifold;
  private final HsTransport hsTransport;

  public DoubleExponential(HsManifold hsManifold, HsTransport hsTransport) {
    this.hsManifold = Objects.requireNonNull(hsManifold);
    this.hsTransport = Objects.requireNonNull(hsTransport);
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
      TensorUnaryOperator operator = hsTransport.shift(x, xv);
      return w -> exp_xv.exp(operator.apply(w));
    }
  }
}
