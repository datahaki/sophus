// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

import ch.alpine.sophus.api.TangentSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** Reference:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020, p. 4 */
public record DoubleExponential(HomogeneousSpace homogeneousSpace) implements Serializable {
  public DoubleExponentialPoint at(Tensor x) {
    return new DoubleExponentialPoint(x);
  }

  public class DoubleExponentialPoint implements Serializable {
    private final Tensor p;
    private final TangentSpace tangentSpace;

    private DoubleExponentialPoint(Tensor p) {
      this.p = p;
      tangentSpace = homogeneousSpace.tangentSpace(p);
    }

    public TensorUnaryOperator operator(Tensor v) {
      Tensor q = tangentSpace.exp(v);
      TangentSpace exp_q = homogeneousSpace.tangentSpace(q);
      TensorUnaryOperator operator = homogeneousSpace.hsTransport().shift(p, q);
      return w -> exp_q.exp(operator.apply(w));
    }
  }
}
