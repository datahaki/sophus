// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.OptionalInt;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.RotateLeft;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Integers;

/** for k == 0 the coordinates are identical to three-point coordinates with mean value as barycenter
 * 
 * mean value coordinates are C^\infty and work for non-convex polygons
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020 */
public class IterativeCoordinate implements Genesis, Serializable {
  /** @param genesis
   * @param k non-negative
   * @return */
  public static Genesis of(Genesis genesis, int k) {
    return new IterativeCoordinate(Objects.requireNonNull(genesis), k);
  }

  /** @param k non-negative
   * @return */
  public static Genesis meanValue(int k) {
    return k == 0 //
        ? ThreePointCoordinate.of(Barycenter.MEAN_VALUE)
        : new IterativeCoordinate(ThreePointWeighting.of(Barycenter.MEAN_VALUE), k);
  }

  /***************************************************/
  private final Genesis genesis;
  private final int k;

  /** @param genesis
   * @param k non-negative */
  /* package */ IterativeCoordinate(Genesis genesis, int k) {
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
      Tensor midpoints = Adds.of(normalized);
      Tensor scaling = InverseNorm.INSTANCE.origin(midpoints);
      // OptionalInt optionalInt = NormalizeTotal.indeterminate(scaling);
      // if (optionalInt.isPresent())
      // return Array.fill(() -> DoubleScalar.INDETERMINATE, normalized.length());
      normalized = scaling.pmul(midpoints);
      deque.push(scaling);
    }
    Tensor weights = genesis.origin(normalized);
    while (!deque.isEmpty())
      weights = RotateLeft.of(Adds.of(deque.pop().pmul(weights)), -1);
    return weights;
  }
}
