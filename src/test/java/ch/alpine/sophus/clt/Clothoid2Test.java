// code by jph
package ch.alpine.sophus.clt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarTensorFunction;

public class Clothoid2Test {
  @Test
  public void testSimple() {
    ScalarTensorFunction scalarTensorFunction = Clothoid2.INSTANCE.curve(Tensors.vector(1, 2, 3), Array.zeros(3));
    assertTrue(scalarTensorFunction instanceof ClothoidCurve2);
  }
}
