// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class RnNormTest extends TestCase {
  public void testSimple() {
    Scalar two = RnNorm.INSTANCE.norm(Tensors.vector(1, 1, 1, 1));
    ExactScalarQ.require(two);
    assertEquals(two, RealScalar.of(2));
  }
}
