// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactScalarQ;

class RnMetricSquaredTest {
  @Test
  void testSimple() {
    Scalar scalar = RnMetricSquared.INSTANCE.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 2, 3));
    ExactScalarQ.require(scalar);
    assertEquals(scalar, RealScalar.of(3 * 3 + 2 * 2));
  }
}
