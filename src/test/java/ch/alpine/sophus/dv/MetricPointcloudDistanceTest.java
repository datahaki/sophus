// code by jph
package ch.alpine.sophus.dv;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.sca.Clips;

class MetricPointcloudDistanceTest {
  @Test
  void testSimple() {
    TensorScalarFunction tensorScalarFunction = new MetricPointcloudDistance(CirclePoints.of(20), RnGroup.INSTANCE);
    Scalar distance = tensorScalarFunction.apply(Tensors.vector(1, 1));
    Clips.interval(0.4, 0.5).requireInside(distance);
  }
}
