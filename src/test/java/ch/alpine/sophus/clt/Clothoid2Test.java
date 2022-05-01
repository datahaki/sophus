// code by jph
package ch.alpine.sophus.clt;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarTensorFunction;

class Clothoid2Test {
  @Test
  public void testSimple() {
    ScalarTensorFunction scalarTensorFunction = Clothoid2.INSTANCE.curve(Tensors.vector(1, 2, 3), Array.zeros(3));
    assertInstanceOf(ClothoidCurve2.class, scalarTensorFunction);
  }
}
