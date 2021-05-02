// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class RnMetricTest extends TestCase {
  public void testSimple() {
    TensorMetric tensorMetric = RnMetric.INSTANCE;
    Scalar scalar = tensorMetric.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 4, 3));
    assertEquals(scalar, RealScalar.of(5));
    ExactScalarQ.require(scalar);
  }
}
