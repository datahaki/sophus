// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.OptionalInt;

import ch.ethz.idsc.sophus.gbc.AffineCoordinate;
import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Integers;

/** References:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020
 * 
 * @see InsidePolygonCoordinate */
public class IterativeCoordinate implements Genesis, Serializable {
  /** genesis can be for instance {@link ThreePointCoordinate}, or {@link AffineCoordinate}
   * 
   * @param genesis
   * @param k non-negative
   * @return */
  public static Genesis of(Genesis genesis, int k) {
    return new IterativeCoordinate(Objects.requireNonNull(genesis), k);
  }

  /***************************************************/
  private final Genesis genesis;
  private final int k;

  /** @param genesis
   * @param k non-negative */
  private IterativeCoordinate(Genesis genesis, int k) {
    this.genesis = genesis;
    this.k = Integers.requirePositiveOrZero(k);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor scaling = InverseNorm.INSTANCE.origin(levers);
    OptionalInt optionalInt = NormalizeTotal.indeterminate(scaling);
    return optionalInt.isPresent() //
        ? UnitVector.of(levers.length(), optionalInt.getAsInt())
        : NormalizeTotal.FUNCTION.apply(scaling.pmul(iterate(scaling.pmul(levers))));
  }

  /** @param normalized points on circle
   * @return homogeneous coordinates */
  private Tensor iterate(Tensor normalized) {
    Deque<Tensor> deque = new ArrayDeque<>(k);
    for (int depth = 0; depth < k; ++depth) {
      Tensor midpoints = Adds.forward(normalized);
      Tensor scaling = InverseNorm.INSTANCE.origin(midpoints);
      // OptionalInt optionalInt = NormalizeTotal.indeterminate(scaling);
      // if (optionalInt.isPresent())
      // return Array.fill(() -> DoubleScalar.INDETERMINATE, normalized.length());
      normalized = scaling.pmul(midpoints);
      deque.push(scaling);
    }
    Tensor weights = genesis.origin(normalized);
    while (!deque.isEmpty())
      weights = Adds.reverse(deque.pop().pmul(weights));
    return weights;
  }
}
