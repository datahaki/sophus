// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Integers;

/** References:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020
 * 
 * "Inverse Distance Coordinates for Scattered Sets of Points"
 * by Jan Hakenberg, 2020
 * 
 * @see InsidePolygonCoordinate */
public class IteratyveCoordinate implements Genesis, Serializable {
  /** @param genesis
   * @param k non-negative
   * @return */
  public static Genesis of(Genesis genesis, int k) {
    return new IteratyveCoordinate(Objects.requireNonNull(genesis), k);
  }

  /***************************************************/
  private final Genesis genesis;
  private final int k;

  /** @param genesis
   * @param k non-negative */
  /* package */ IteratyveCoordinate(Genesis genesis, int k) {
    this.genesis = genesis;
    this.k = Integers.requirePositiveOrZero(k);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Deque<Tensor> deque = new ArrayDeque<>(k);
    for (int depth = 0; depth < k; ++depth) {
      Tensor scaling = InverseNorm.INSTANCE.origin(levers);
      levers = scaling.pmul(levers);
      levers = Adds.forward(levers);
      deque.push(scaling);
    }
    Tensor weights = genesis.origin(levers);
    while (!deque.isEmpty())
      weights = deque.pop().pmul(Adds.reverse(weights));
    return weights;
  }
}
