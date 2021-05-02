// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarTensorFunction;
import junit.framework.TestCase;

public class Clothoid2Test extends TestCase {
  public void testSimple() {
    ScalarTensorFunction scalarTensorFunction = Clothoid2.INSTANCE.curve(Tensors.vector(1, 2, 3), Array.zeros(3));
    assertTrue(scalarTensorFunction instanceof ClothoidCurve2);
  }
}
