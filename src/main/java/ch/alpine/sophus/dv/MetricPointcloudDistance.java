// code by mg, jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.math.api.TensorMetric;
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
// TODO SOPHUS ALG use distance vectors instead
public record MetricPointcloudDistance(Tensor points, TensorMetric tensorMetric) implements TensorScalarFunction {
  public MetricPointcloudDistance {
    Objects.requireNonNull(points);
    Objects.requireNonNull(tensorMetric);
  }

  @Override
  public Scalar apply(Tensor point) {
    return points.stream() //
        .map(vector -> tensorMetric.distance(point, vector)) //
        .min(Scalars::compare) //
        .orElseThrow();
  }
}
