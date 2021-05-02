// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import junit.framework.TestCase;

public class RnNormSquaredTest extends TestCase {
  public void testSimple() {
    Scalar norm = Vector2NormSquared.of(Tensors.vector(3, 4));
    assertEquals(ExactScalarQ.require(norm), RealScalar.of(9 + 16));
  }
}
