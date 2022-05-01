// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactScalarQ;

public class RnMetricTest {
  @Test
  public void testSimple() {
    TensorMetric tensorMetric = RnMetric.INSTANCE;
    Scalar scalar = tensorMetric.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 4, 3));
    assertEquals(scalar, RealScalar.of(5));
    ExactScalarQ.require(scalar);
  }
}
