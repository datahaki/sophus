// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.MemberQ;
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
import junit.framework.TestCase;

public class HnBilinearFormTest extends TestCase {
  private static final MemberQ MEMBER_Q = HnMemberQ.of(Tolerance.CHOP);

  public void testNegative() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      MEMBER_Q.requirePoint(p);
      MEMBER_Q.requirePoint(q);
      Scalar pq = HnBilinearForm.between(p, q);
      assertTrue(Scalars.lessEquals(pq, RealScalar.ONE.negate()));
    }
  }

  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor J = IdentityMatrix.of(d + 1);
      J.set(Scalar::negate, d, d);
      SymmetricMatrixQ.require(J);
      for (int count = 0; count < 10; ++count) {
        Tensor p = RandomVariate.of(distribution, d + 1);
        Tensor q = RandomVariate.of(distribution, d + 1);
        Tolerance.CHOP.requireClose( //
            HnBilinearForm.between(p, q), //
            J.dot(p).dot(q));
      }
    }
  }

  public void testFail() {
    try {
      HnBilinearForm.between(Tensors.vector(1, 2, 3), Tensors.vector(1, 2, 3, 4));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
