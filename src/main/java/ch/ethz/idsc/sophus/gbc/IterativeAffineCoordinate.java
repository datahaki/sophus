// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.sca.Exp;

/** attempts to produce positive weights for levers with zero in convex hull */
public class IterativeAffineCoordinate implements Genesis, Serializable {
  private static final Genesis GENESIS = AffineCoordinate.INSTANCE;
  // ---
  private final int k;
  private final Scalar sigma;

  public IterativeAffineCoordinate(int k, Scalar sigma) {
    this.k = Integers.requirePositiveOrZero(k);
    this.sigma = Objects.requireNonNull(sigma);
  }

  /** Properties:
   * strictly positive
   * monotonous
   * smooth
   * 0 maps to 1
   * 
   * @param scalar
   * @return */
  private Scalar amplify(Scalar scalar) {
    return Exp.FUNCTION.apply(sigma.multiply(scalar));
  }

  public Deque<Tensor> factors(Tensor levers) {
    Deque<Tensor> deque = new ArrayDeque<>(k + 1);
    deque.add(ConstantArray.of(RealScalar.ONE, levers.length()));
    Tensor average = AveragingWeights.INSTANCE.origin(levers); // setpoint
    for (int depth = 0; depth < k; ++depth) {
      Tensor factor = deque.peekLast();
      deque.add(factor.pmul(GENESIS.origin(factor.pmul(levers)).subtract(average).map(this::amplify)));
    }
    return deque;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor factor = factors(levers).peekLast();
    return NormalizeTotal.FUNCTION.apply(factor.pmul(GENESIS.origin(factor.pmul(levers))));
  }
}
