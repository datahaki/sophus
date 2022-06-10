// code by jph
package ch.alpine.sophus.gbc.it;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import ch.alpine.sophus.gbc.AffineCoordinate;
import ch.alpine.sophus.hs.Genesis;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.red.Times;

/** attempts to produce positive weights for levers with zero in convex hull */
public record IterativeAffineCoordinate(TensorUnaryOperator amplifier, int k) implements GenesisDeque, Serializable {

  private static final Genesis GENESIS = AffineCoordinate.INSTANCE;
  public IterativeAffineCoordinate {
    Objects.requireNonNull(amplifier);
    Integers.requirePositiveOrZero(k);
  }

  @Override
  public Deque<WeightsFactors> deque(Tensor levers) {
    Deque<WeightsFactors> deque = new ArrayDeque<>();
    Tensor factors = ConstantArray.of(RealScalar.ONE, levers.length());
    for (int depth = 0; depth <= k; ++depth) {
      // should converge to uniform vector
      Tensor uniform = GENESIS.origin(Times.of(factors, levers));
      Tensor weights = NormalizeTotal.FUNCTION.apply(Times.of(factors, uniform));
      if (!deque.isEmpty() && Tolerance.CHOP.isClose(weights, deque.peekLast().weights())) {
        deque.add(new WeightsFactors(weights, factors));
        break;
      }
      deque.add(new WeightsFactors(weights, factors));
      factors = Times.of(factors, amplifier.apply(uniform));
    }
    return deque;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return deque(levers).peekLast().weights();
  }
}
