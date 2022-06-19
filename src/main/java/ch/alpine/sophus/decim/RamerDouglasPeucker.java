// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Sign;

/** Generalization of the Ramer-Douglas-Peucker algorithm
 * 
 * Quote: "The Ramer-Douglas-Peucker algorithm decimates a curve composed of line segments
 * to a similar curve with fewer points. [...] The algorithm defines 'dissimilar' based
 * on the maximum distance between the original curve and the simplified curve. [...]
 * The expected complexity of this algorithm can be described by the linear recurrence
 * T(n) = 2 * T(​n/2) + O(n), which has the well-known solution O(n * log n). However, the
 * worst-case complexity is O(n^2)."
 * 
 * https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm */
/* package */ record RamerDouglasPeucker(LineDistance lineDistance, Scalar epsilon) implements CurveDecimation {
  public RamerDouglasPeucker {
    Sign.requirePositiveOrZero(epsilon);
  }

  @Override
  public Tensor apply(Tensor tensor) {
    return evaluate(tensor).result();
  }

  @Override // from CurveDecimation
  public DecimationResult evaluate(Tensor tensor) {
    return Tensors.isEmpty(tensor) //
        ? DecimationResult.EMPTY
        : new SpaceResult(tensor).getDecimationResult();
  }

  private class SpaceResult implements Serializable {
    private final Tensor[] tensors;
    private final Scalar[] scalars;
    private final List<Integer> list = new LinkedList<>();

    /** @param tensor of length at least 1 */
    public SpaceResult(Tensor tensor) {
      tensors = tensor.stream().toArray(Tensor[]::new);
      scalars = new Scalar[tensors.length];
      int end = tensors.length - 1;
      recur(0, end);
      scalars[end] = epsilon.zero();
      if (0 < end)
        list.add(end);
    }

    private void recur(int beg, int end) {
      Scalar max = epsilon.zero();
      scalars[beg] = max;
      if (beg + 1 < end) { // at least one element in between beg and end
        TensorNorm tensorNorm = lineDistance.tensorNorm(tensors[beg], tensors[end]);
        int mid = -1;
        for (int index = beg + 1; index < end; ++index) {
          Scalar dist = tensorNorm.norm(tensors[index]);
          scalars[index] = dist;
          if (Scalars.lessThan(max, dist)) {
            max = dist;
            mid = index;
          }
        }
        if (Scalars.lessThan(epsilon, max)) {
          recur(beg, mid);
          recur(mid, end);
          return;
        }
      }
      list.add(beg);
    }

    public DecimationResult getDecimationResult() {
      return new DecimationResult( //
          Tensor.of(list.stream().map(i -> tensors[i]).map(Tensor::copy)), //
          Tensors.of(scalars));
    }
  }
}
