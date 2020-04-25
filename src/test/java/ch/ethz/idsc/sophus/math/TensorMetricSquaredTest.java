// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class TensorMetricSquaredTest extends TestCase {
  public void testRn() {
    Tensor p = Tensors.vector(1, 2, 3, 4);
    Tensor q = Tensors.vector(2, -1, -7, 9);
    Scalar d1 = RnMetric.INSTANCE.distance(p, q);
    TensorMetric tensorMetric = TensorMetricSquared.of(RnMetric.INSTANCE);
    Scalar d2 = tensorMetric.distance(p, q);
    Tolerance.CHOP.requireClose(d1.multiply(d1), d2);
  }

  public void testFailNull() {
    try {
      TensorMetricSquared.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
