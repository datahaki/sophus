// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.lie.rn.RnMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class MetricPointcloudDistanceTest extends TestCase {
  public void testSimple() {
    TensorScalarFunction tensorScalarFunction = new MetricPointcloudDistance(CirclePoints.of(20), RnMetric.INSTANCE);
    Scalar distance = tensorScalarFunction.apply(Tensors.vector(1, 1));
    Clips.interval(0.4, 0.5).requireInside(distance);
  }
}
