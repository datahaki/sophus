// code by jph
package ch.alpine.sophus.lie;

import java.util.function.BinaryOperator;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class LieAlgebraImplTest extends TestCase {
  public void testSimple() {
    LieAlgebraImpl lieAlgebraImpl = new LieAlgebraImpl(LieAlgebras.he1());
    BinaryOperator<Tensor> bch = lieAlgebraImpl.bch(20);
    Tensor z = bch.apply(Tensors.vector(1, 2, 3), Tensors.vector(4, -3, -8));
    ExactTensorQ.require(z);
    assertEquals(z, Tensors.fromString("{5, -1, -21/2}"));
  }
}
