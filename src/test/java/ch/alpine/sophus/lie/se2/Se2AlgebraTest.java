// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import junit.framework.TestCase;

public class Se2AlgebraTest extends TestCase {
  /** @return ad tensor of 3-dimensional se(2) */
  private static final Scalar P1 = RealScalar.ONE;
  private static final Scalar N1 = RealScalar.ONE.negate();

  private static Tensor se2() {
    Tensor ad = Array.zeros(3, 3, 3);
    ad.set(N1, 1, 2, 0);
    ad.set(P1, 1, 0, 2);
    ad.set(N1, 0, 1, 2);
    ad.set(P1, 0, 2, 1);
    return ad;
  }

  public void testSimple() {
    Tensor ad = Tensors.fromString( //
        "{{{0, 0, 0}, {0, 0, -1}, {0, 1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}}");
    ExactTensorQ.require(Se2Algebra.INSTANCE.ad());
    assertEquals(Se2Algebra.INSTANCE.ad(), ad);
    assertEquals(Se2Algebra.INSTANCE.ad(), se2());
  }
}
