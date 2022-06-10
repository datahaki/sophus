// code by jph
package ch.alpine.sophus.bm;

import java.io.Serializable;
import java.util.Objects;
import java.util.PriorityQueue;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.spd.SpdPhongMean;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.sca.Abs;

/** approximation of biinvariant mean using a geodesic average that has a
 * nested structure based on the weights.
 * 
 * because the result is not invariant under reordering of the input, the
 * implementation should only be used as an initial guess for the iterative
 * fixed point method.
 * 
 * tests have shown empirically for the SPD manifold, that the reducing
 * mean is closer to the exact mean than the {@link SpdPhongMean}
 * 
 * For R^n the ReducingMean coincides with the weighted average.
 * 
 * @see IterativeBiinvariantMean */
/* package */ record ReducingMean(GeodesicSpace geodesicSpace) implements BiinvariantMean, Serializable {

  /** @param geodesicSpace
   * @return */
  public static BiinvariantMean of(GeodesicSpace geodesicSpace) {
    return new ReducingMean(Objects.requireNonNull(geodesicSpace));
  }

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    VectorQ.requireLength(weights, sequence.length());
    PriorityQueue<WPoint> priorityQueue = new PriorityQueue<>();
    int index = 0;
    for (Tensor point : sequence) {
      Scalar weight = weights.Get(index);
      if (Scalars.nonZero(weight))
        priorityQueue.add(new WPoint(weight, point));
      ++index;
    }
    while (1 < priorityQueue.size()) {
      WPoint wPoint0 = priorityQueue.poll(); // may be zero
      WPoint wPoint1 = priorityQueue.poll(); // guaranteed to be non-zero
      boolean s01 = wPoint0.split(wPoint1, priorityQueue);
      if (!s01) {
        WPoint wPoint2 = priorityQueue.poll();
        boolean s02 = wPoint0.split(wPoint2, priorityQueue);
        if (s02)
          priorityQueue.add(wPoint1);
        else {
          boolean s12 = wPoint1.split(wPoint2, priorityQueue);
          if (s12)
            priorityQueue.add(wPoint0);
          else
            throw new IllegalStateException();
        }
      }
    }
    return priorityQueue.poll().point;
  }
  private class WPoint implements Comparable<WPoint> {
    private final Scalar weight;
    private final Tensor point;

    public WPoint(Scalar weight, Tensor point) {
      this.weight = weight;
      this.point = point;
    }

    public boolean split(WPoint wPoint, PriorityQueue<WPoint> priorityQueue) {
      Scalar total = weight.add(wPoint.weight);
      if (Scalars.nonZero(total)) {
        Tensor split = geodesicSpace.split(point, wPoint.point, wPoint.weight.divide(total));
        priorityQueue.add(new WPoint(total, split));
        return true;
      }
      return false;
    }

    @Override
    public int compareTo(WPoint wPoint) {
      return Scalars.compare(Abs.FUNCTION.apply(weight), Abs.FUNCTION.apply(wPoint.weight));
    }
  }
}
