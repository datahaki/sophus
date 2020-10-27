// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.AffineCoordinate;
import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
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

  /** @param genesis
   * @param k non-negative */
  private ExponentialCoordinate(Genesis genesis, int k) {
    this.genesis = genesis;
    this.k = Integers.requirePositiveOrZero(k);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor factor = ConstantArray.of(RealScalar.ONE, levers.length());
    for (int depth = 0; depth < k; ++depth) {
//      Tensor scaling = NormalizeTotal.FUNCTION.apply(AffineCoordinate.INSTANCE.origin(levers).map(Exp.FUNCTION));
      Tensor scaling = NormalizeTotal.FUNCTION.apply(genesis.origin(levers).map(Exp.FUNCTION));
      levers = scaling.pmul(levers);
      factor = factor.pmul(scaling);
    }
    return NormalizeTotal.FUNCTION.apply(factor.pmul(genesis.origin(levers)));
  }
}
