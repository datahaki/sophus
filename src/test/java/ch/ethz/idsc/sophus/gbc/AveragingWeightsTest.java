// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class AveragingWeightsTest extends TestCase {
  public void testSimple() {
    assertEquals(AveragingWeights.INSTANCE.origin(Tensors.vector(1, 2, 3)), Tensors.fromString("{1/3,1/3,1/3}"));
  }
}
