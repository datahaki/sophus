// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.mat.Tolerance;

/** attempts to produce positive weights for levers with zero in convex hull */
public class IterativeAffineCoordinate implements GenesisDeque, Serializable {
  private static final Genesis GENESIS = AffineCoordinate.INSTANCE;
  // ---
  private final TensorUnaryOperator amplifier;
  private final int k;

  public IterativeAffineCoordinate(TensorUnaryOperator amplifier, int k) {
    this.amplifier = Objects.requireNonNull(amplifier);
    this.k = Integers.requirePositiveOrZero(k);
  }

  @Override
  public Deque<Evaluation> deque(Tensor levers) {
    Deque<Evaluation> deque = new ArrayDeque<>();
    Tensor factors = ConstantArray.of(RealScalar.ONE, levers.length());
    for (int depth = 0; depth <= k; ++depth) {
      // should converge to uniform vector
      Tensor uniform = GENESIS.origin(factors.pmul(levers));
      Tensor weights = NormalizeTotal.FUNCTION.apply(factors.pmul(uniform));
      if (!deque.isEmpty() && Tolerance.CHOP.isClose(weights, deque.peekLast().weights())) {
        deque.add(new Evaluation(weights, factors));
        break;
      }
      deque.add(new Evaluation(weights, factors));
      factors = factors.pmul(amplifier.apply(uniform));
    }
    return deque;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return deque(levers).peekLast().weights();
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
