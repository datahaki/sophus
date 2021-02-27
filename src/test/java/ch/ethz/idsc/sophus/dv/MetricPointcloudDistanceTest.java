// code by jph
package ch.ethz.idsc.sophus.dv;

import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class MetricPointcloudDistanceTest extends TestCase {
  public void testSimple() {
    TensorScalarFunction tensorScalarFunction = MetricPointcloudDistance.of(CirclePoints.of(20), RnMetric.INSTANCE);
    Scalar distance = tensorScalarFunction.apply(Tensors.vector(1, 1));
    Clips.interval(0.4, 0.5).requireInside(distance);
  }
}
