// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** Reference:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020, p. 4 */
public class DoubleExponential implements Serializable {
  private static final long serialVersionUID = 1998495011256776356L;
  // ---
  private final HsExponential hsExponential;
  private final HsTransport hsTransport;

  public DoubleExponential(HsExponential hsExponential, HsTransport hsTransport) {
    this.hsExponential = Objects.requireNonNull(hsExponential);
    this.hsTransport = Objects.requireNonNull(hsTransport);
  }

  public DoubleExponentialPoint at(Tensor x) {
    return new DoubleExponentialPoint(x);
  }

  public class DoubleExponentialPoint implements Serializable {
    private static final long serialVersionUID = -6371548454280492599L;
    // ---
    private final Tensor x;
    private final Exponential exponential;

    private DoubleExponentialPoint(Tensor x) {
      this.x = x;
      exponential = hsExponential.exponential(x);
    }

    public TensorUnaryOperator operator(Tensor v) {
      Tensor xv = exponential.exp(v);
      Exponential exp_xv = hsExponential.exponential(xv);
      TensorUnaryOperator operator = hsTransport.shift(x, xv);
      return w -> exp_xv.exp(operator.apply(w));
    }
  }
}
