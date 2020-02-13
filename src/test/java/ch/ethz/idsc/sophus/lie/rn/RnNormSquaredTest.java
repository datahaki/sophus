// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class RnNormSquaredTest extends TestCase {
  public void testSimple() {
    ExactScalarQ.require(RnNormSquared.INSTANCE.norm(Tensors.vector(3, 4)));
  }
}
