// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.nrm.Vector2Norm;
import junit.framework.TestCase;

public class RnNormTest extends TestCase {
  public void testSimple() {
    Scalar two = Vector2Norm.of(Tensors.vector(1, 1, 1, 1));
    ExactScalarQ.require(two);
    assertEquals(two, RealScalar.of(2));
  }
}
