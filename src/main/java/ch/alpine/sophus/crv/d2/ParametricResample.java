// code by jph
package ch.alpine.sophus.crv.d2;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.nrm.Vector2Norm;

/** method of equidistant resampling of a sequence of irregular spaced points */
// TODO generalize to Rn
public class ParametricResample implements Serializable {
  /** determines whether points are connected */
  private final Scalar threshold;
  /** distance between samples after re-sampling */
  private final Scalar ds;
  public int minLength = 2;

  /** the threshold
   * 
   * @param threshold a common value is RealScalar.of(33)
   * @param ds distance between samples after re-sampling */
  public ParametricResample(Scalar threshold, Scalar ds) {
    this.threshold = threshold;
    this.ds = ds;
  }

  /** @param points sequence of lidar points in ccw- or cw-direction
   * @return points grouped by connectivity and resampled equidistantly */
  public ResampleResult apply(Tensor points) {
    Tensor dista = Tensor.of(points.stream().map(Vector2Norm::of)); // distance of points from origin
    Tensor diffs = Differences.of(points); // displacement vectors
    Tensor delta = Tensor.of(diffs.stream().map(Vector2Norm::of)); // length of displacement vectors
    List<Tensor> list = new LinkedList<>(); // return structure
    Tensor ret = Tensors.empty(); // initialize connected segment
    Scalar sum = RealScalar.ZERO; // parameter
    for (int index = 0; index < diffs.length(); ++index) { // iterate along displacement vectors
      boolean connected = Scalars.lessThan( // criterion if two adjacent points belong to connected region
          delta.Get(index).multiply(threshold), //
          dista.Get(index + 0).add(dista.Get(index + 1)));
      if (connected) {
        final Scalar length = delta.Get(index);
        while (Scalars.lessThan(sum, length)) {
          // 0 <= sum < length implies 0 <= sum / length < 1
          ret.append(RealScalar.of(index).add(sum.divide(length)));
          sum = sum.add(ds);
        }
        sum = sum.subtract(length);
      } else {
        if (addPredicate(ret))
          list.add(ret);
        ret = Tensors.empty();
        sum = RealScalar.ZERO;
      }
    }
    if (addPredicate(ret))
      list.add(ret);
    return new ResampleResult(points, list);
  }

  private boolean addPredicate(Tensor tensor) {
    return minLength <= tensor.length();
  }
}
