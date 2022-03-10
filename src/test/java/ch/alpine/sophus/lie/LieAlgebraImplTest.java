// code by jph
package ch.alpine.sophus.lie;

import java.util.Random;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.se3.Se3Algebra;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
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

  private static final LieAlgebra[] HS_ALGEBRAS = { //
      So3Algebra.INSTANCE, //
      Se2Algebra.INSTANCE, //
      Se3Algebra.INSTANCE, //
      new HeAlgebra(1), //
      new HeAlgebra(2), //
      Sl2Algebra.INSTANCE };

  public void testSign() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Random random = new Random();
    for (LieAlgebra hsAlgebra : HS_ALGEBRAS) {
      int dimG = hsAlgebra.ad().length();
      BinaryOperator<Tensor> bch = hsAlgebra.bch(6);
      for (int count = 0; count < 5; ++count) {
        Tensor a = RandomVariate.of(distribution, random, dimG);
        Tensor b = RandomVariate.of(distribution, random, dimG);
        // (a b)' == b' a'
        Tolerance.CHOP.requireClose(bch.apply(a, b).negate(), bch.apply(b.negate(), a.negate()));
        // (a' b)' == b' a
        Tolerance.CHOP.requireClose(bch.apply(a.negate(), b).negate(), bch.apply(b.negate(), a));
        // (a' b) == (b' a)'
        Tolerance.CHOP.requireClose(bch.apply(a.negate(), b), bch.apply(b.negate(), a).negate());
      }
    }
  }
}
