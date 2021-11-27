// code by jph
package ch.alpine.sophus.gbc.d2;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.OptionalInt;

import ch.alpine.sophus.gbc.AffineCoordinate;
import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.red.Times;

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

  // ---
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
        : NormalizeTotal.FUNCTION.apply(Times.of(scaling, iterate(Times.of(scaling, levers))));
  }

  /** @param normalized points on circle
   * @return homogeneous coordinates */
  private Tensor iterate(Tensor normalized) {
    Deque<Tensor> deque = new ArrayDeque<>(k);
    for (int depth = 0; depth < k; ++depth) {
      Tensor midpoints = Adds.forward(normalized);
      Tensor scaling = InverseNorm.INSTANCE.origin(midpoints);
      normalized = Times.of(scaling, midpoints);
      deque.push(scaling);
    }
    Tensor weights = genesis.origin(normalized);
    while (!deque.isEmpty())
      weights = Adds.reverse(Times.of(deque.pop(), weights));
    return weights;
  }
}
