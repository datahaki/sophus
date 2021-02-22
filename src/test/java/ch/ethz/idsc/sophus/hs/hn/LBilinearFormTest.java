// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.TrapezoidalDistribution;
import junit.framework.TestCase;

public class LBilinearFormTest extends TestCase {
  public void testSimple() {
    Distribution distribution = TrapezoidalDistribution.of(-3, -1, 1, 3);
    for (int count = 0; count < 5; ++count) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      assertTrue(Scalars.lessThan(LBilinearForm.between(p, q), RealScalar.of(-1)));
    }
  }

  public void testNegative() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      HnMemberQ.INSTANCE.require(p);
      HnMemberQ.INSTANCE.require(q);
      Scalar pq = LBilinearForm.between(p, q);
      assertTrue(Scalars.lessEquals(pq, RealScalar.ONE.negate()));
    }
  }

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

  public void testFail() {
    AssertFail.of(() -> LBilinearForm.between(Tensors.vector(1, 2, 3), Tensors.vector(1, 2, 3, 4)));
  }
}
