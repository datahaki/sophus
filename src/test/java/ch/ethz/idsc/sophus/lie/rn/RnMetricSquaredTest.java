// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class RnMetricSquaredTest extends TestCase {
  public void testSimple() {
    Scalar scalar = RnMetricSquared.INSTANCE.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 2, 3));
    ExactScalarQ.require(scalar);
    assertEquals(scalar, RealScalar.of(3 * 3 + 2 * 2));
  }
}
