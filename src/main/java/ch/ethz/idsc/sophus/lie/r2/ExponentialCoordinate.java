// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.sca.Exp;

/**  */
public class ExponentialCoordinate implements Genesis, Serializable {
  /** @param genesis
   * @param k non-negative
   * @return */
  public static Genesis of(Genesis genesis, int k) {
    return new ExponentialCoordinate(Objects.requireNonNull(genesis), k);
  }

  /***************************************************/
  private final Genesis genesis;
  private final int k;
  private final Scalar scalar;

  /** @param genesis
   * @param k non-negative */
  private ExponentialCoordinate(Genesis genesis, int k) {
    this.genesis = genesis;
    this.k = Integers.requirePositiveOrZero(k);
    scalar = RealScalar.of(3);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    int n = levers.length();
    Tensor average = ConstantArray.of(RationalScalar.of(1, n), n);
    Tensor factors = ConstantArray.of(RealScalar.ONE, n);
    Tensor current = levers;
    for (int depth = 0; depth < k; ++depth) {
      Tensor scaling = genesis.origin(current).subtract(average).multiply(scalar).map(Exp.FUNCTION);
      factors = factors.pmul(scaling);
      current = factors.pmul(levers);
    }
    // normalize total is not necessary algebraically
    return NormalizeTotal.FUNCTION.apply(factors.pmul(genesis.origin(current)));
  }
}
