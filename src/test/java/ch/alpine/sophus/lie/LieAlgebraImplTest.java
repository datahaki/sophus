// code by jph
package ch.alpine.sophus.lie;

import java.util.function.BinaryOperator;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import junit.framework.TestCase;

public class LieAlgebraImplTest extends TestCase {
  private static final Scalar P1 = RealScalar.ONE;
  private static final Scalar N1 = RealScalar.ONE.negate();

  /** @return ad tensor of 3-dimensional Heisenberg Lie-algebra */
  private static Tensor he1() {
    Tensor ad = Array.zeros(3, 3, 3);
    ad.set(P1, 2, 1, 0);
    ad.set(N1, 2, 0, 1);
    return ad;
  }

  public void testSimple() {
    LieAlgebraImpl lieAlgebraImpl = new LieAlgebraImpl(he1());
    BinaryOperator<Tensor> bch = lieAlgebraImpl.bch(20);
    Tensor z = bch.apply(Tensors.vector(1, 2, 3), Tensors.vector(4, -3, -8));
    ExactTensorQ.require(z);
    assertEquals(z, Tensors.fromString("{5, -1, -21/2}"));
  }
}
