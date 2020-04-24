// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class RnNormSquaredTest extends TestCase {
  public void testSimple() {
    Scalar norm = RnNormSquared.INSTANCE.norm(Tensors.vector(3, 4));
    assertEquals(ExactScalarQ.require(norm), RealScalar.of(9 + 16));
  }
}
