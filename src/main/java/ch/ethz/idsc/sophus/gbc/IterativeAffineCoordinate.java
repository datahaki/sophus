// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.ext.Integers;

/** attempts to produce positive weights for levers with zero in convex hull */
public class IterativeAffineCoordinate implements Genesis, Serializable {
  private static final Genesis GENESIS = AffineCoordinate.INSTANCE;
  // ---
  private final ScalarUnaryOperator amplifier;
  private final int k;

  public IterativeAffineCoordinate(ScalarUnaryOperator amplifier, int k) {
    this.amplifier = amplifier;
    this.k = Integers.requirePositiveOrZero(k);
  }

  public Deque<Tensor> factors(Tensor levers) {
    Deque<Tensor> deque = new ArrayDeque<>(k + 1);
    deque.add(ConstantArray.of(RealScalar.ONE, levers.length()));
    Tensor average = AveragingWeights.INSTANCE.origin(levers);
    for (int depth = 0; depth < k; ++depth) {
      Tensor factor = deque.peekLast();
      deque.add(factor.pmul(GENESIS.origin(factor.pmul(levers)).subtract(average).map(amplifier)));
    }
    return deque;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor factor = factors(levers).peekLast();
    return NormalizeTotal.FUNCTION.apply(factor.pmul(GENESIS.origin(factor.pmul(levers))));
  }
}
