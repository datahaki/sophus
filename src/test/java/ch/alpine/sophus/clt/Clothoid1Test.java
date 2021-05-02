// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.NumberQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarTensorFunction;
import junit.framework.TestCase;

public class Clothoid1Test extends TestCase {
  public void testSimple() {
    ScalarTensorFunction scalarTensorFunction = Clothoid1.INSTANCE.curve(Tensors.vector(1, 2, 3), Array.zeros(3));
    assertTrue(scalarTensorFunction instanceof ClothoidCurve1);
  }

  public void testSingular() {
    Tensor beg = Tensors.vector(1, 2, 3);
    Tensor end = Tensors.vector(1, 2, -1);
    ScalarTensorFunction scalarTensorFunction = Clothoid1.INSTANCE.curve(beg, end);
    assertEquals(beg, scalarTensorFunction.apply(RealScalar.ZERO));
    assertEquals(end, scalarTensorFunction.apply(RealScalar.ONE));
    Tensor curve = Subdivide.of(0.0, 1.0, 50).map(scalarTensorFunction);
    assertTrue(NumberQ.all(curve));
  }
}
