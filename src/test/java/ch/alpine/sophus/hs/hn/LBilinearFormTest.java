// code by jph
package ch.alpine.sophus.hs.hn;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TrapezoidalDistribution;
import ch.alpine.tensor.sca.Sign;

class LBilinearFormTest {
  @Test
  public void testSimple() {
    Distribution distribution = TrapezoidalDistribution.of(-3, -1, 1, 3);
    for (int count = 0; count < 5; ++count) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      assertTrue(Scalars.lessThan(LBilinearForm.between(p, q), RealScalar.of(-1)));
    }
  }

  @Test
  public void testNegative() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      HnMemberQ.INSTANCE.require(p);
      HnMemberQ.INSTANCE.require(q);
      Scalar pq = LBilinearForm.between(p, q);
      assertTrue(Scalars.lessEquals(pq, RealScalar.ONE.negate()));
      Tensor dif = p.subtract(q);
      Scalar dd = LBilinearForm.normSquared(dif);
      Sign.requirePositiveOrZero(dd);
    }
  }

  @Test
  public void testRandom() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor J = IdentityMatrix.of(d + 1);
      J.set(Scalar::negate, d, d);
      SymmetricMatrixQ.require(J);
      for (int count = 0; count < 10; ++count) {
        Tensor p = RandomVariate.of(distribution, d + 1);
        Tensor q = RandomVariate.of(distribution, d + 1);
        Tolerance.CHOP.requireClose( //
            LBilinearForm.between(p, q), //
            J.dot(p).dot(q));
      }
    }
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> LBilinearForm.between(Tensors.vector(1, 2, 3), Tensors.vector(1, 2, 3, 4)));
  }
}
