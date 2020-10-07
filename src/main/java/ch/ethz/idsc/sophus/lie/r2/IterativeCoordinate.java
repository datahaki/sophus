// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.RotateLeft;

/** for k == 0 the coordinates are identical to three-point coordinates with mean value as barycenter
 * 
 * mean value coordinates are C^\infty and work for non-convex polygons
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020 */
public class IterativeCoordinate implements Genesis, Serializable {
  private static final CurveSubdivision MIDPOINTS = ControlMidpoints.of(RnGeodesic.INSTANCE);

  /** @param genesis
   * @param k
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

  /** @param k non-negative */
  /* package */ IterativeCoordinate(Genesis genesis, int k) {
    this.genesis = genesis;
    this.k = Integers.requirePositiveOrZero(k);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor scaling = InverseNorm.INSTANCE.origin(levers);
    return NormalizeTotal.FUNCTION.apply(scaling.pmul(recur(scaling.pmul(levers))));
  }

  /** @param normalized points on circle
   * @return homogeneous coordinates */
  private Tensor recur(Tensor normalized) {
    Deque<Tensor> deque = new ArrayDeque<>(k);
    for (int depth = 0; depth < k; ++depth) {
      Tensor midpoints = MIDPOINTS.cyclic(normalized);
      Tensor scaling = InverseNorm.INSTANCE.origin(midpoints);
      normalized = scaling.pmul(midpoints);
      deque.push(scaling);
    }
    Tensor weights = genesis.origin(normalized);
    while (!deque.isEmpty())
      weights = RotateLeft.of(MIDPOINTS.cyclic(deque.pop().pmul(weights)), -1);
    return weights;
  }
}
