// code by jph
package ch.alpine.sophus.lie.se2c;

import java.util.Arrays;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2CoveringDifferencesTest extends TestCase {
  public void testSimple() {
    Tensor p1 = Tensors.vector(0, 0, -Math.PI);
    Tensor p2 = Tensors.vector(0, 0, +Math.PI);
    Tensor tensor = Se2CoveringDifferences.INSTANCE.apply(Tensors.of(p1, p2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(1, 3));
    Chop._14.requireClose(tensor.get(0), Tensors.vector(0, 0, Math.PI * 2));
  }
}
