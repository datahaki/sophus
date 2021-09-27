// code by mg, jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorScalarFunction;

/** shortest distance from given point to a collection of points
 * 
 * class name reflects that results is obtained in a simple manner:
 * by iterating over all points. Due to the O(n) complexity of the
 * query, the use of the algorithm is not recommended for most
 * applications, perhaps only for testing purpose.
 * 
 * The implementation is used in external libraries.
 * 
 * see RnPointcloudRegion which uses a nd-map */
public class MetricPointcloudDistance implements TensorScalarFunction {
  /** @param points
   * @param tensorMetric
   * @return */
  // TODO use distance vectors instead
  public static TensorScalarFunction of(Tensor points, TensorMetric tensorMetric) {
    return new MetricPointcloudDistance( //
        Objects.requireNonNull(points), //
        Objects.requireNonNull(tensorMetric));
  }

  // ---
  private final Tensor points;
  private final TensorMetric tensorMetric;

  private MetricPointcloudDistance(Tensor points, TensorMetric tensorMetric) {
    this.points = points;
    this.tensorMetric = tensorMetric;
  }

  @Override
  public Scalar apply(Tensor point) {
    return points.stream() //
        .map(vector -> tensorMetric.distance(point, vector)) //
        .min(Scalars::compare).orElseThrow();
  }
}
