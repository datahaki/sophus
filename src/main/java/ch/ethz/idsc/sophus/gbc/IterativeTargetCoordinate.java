// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Integers;

/** attempts to produce positive weights for levers with zero in convex hull */
public class IterativeTargetCoordinate implements Genesis, Serializable {
  private static final Genesis GENESIS = AffineCoordinate.INSTANCE;
  // ---
  // private final TensorUnaryOperator amplifier;
  private final int k;

  public IterativeTargetCoordinate(TensorUnaryOperator amplifier, int k) {
    // this.amplifier = Objects.requireNonNull(amplifier);
    this.k = Integers.requirePositiveOrZero(k);
  }

  public Deque<Evaluation> factors(Tensor levers) {
    ShepardTarget shepardTarget = new ShepardTarget(levers);
    Deque<Evaluation> deque = new ArrayDeque<>(k + 1);
    Tensor factors = ConstantArray.of(RealScalar.ONE, levers.length());
    for (int depth = 0; depth <= k; ++depth) {
      Tensor weights = GENESIS.origin(factors.pmul(levers));
      deque.add(new Evaluation(weights, factors));
      factors = factors.pmul(shepardTarget.apply(weights));
    }
    return deque;
  }

  public Tensor origin(Deque<Evaluation> deque, Tensor levers) {
    Tensor factor = deque.peekLast().factors();
    return NormalizeTotal.FUNCTION.apply(factor.pmul(GENESIS.origin(factor.pmul(levers))));
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return origin(factors(levers), levers);
  }

  public static class Evaluation {
    private final Tensor weights;
    private final Tensor factors;

    public Evaluation(Tensor weights, Tensor factors) {
      this.weights = weights;
      this.factors = factors;
    }

    public Tensor factors() {
      return factors;
    }

    public Tensor weights() {
      return weights;
    }
  }
}
