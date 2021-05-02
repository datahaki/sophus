// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class RnMetricSquaredTest extends TestCase {
  public void testSimple() {
    Scalar scalar = RnMetricSquared.INSTANCE.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 2, 3));
    ExactScalarQ.require(scalar);
    assertEquals(scalar, RealScalar.of(3 * 3 + 2 * 2));
  }
}
