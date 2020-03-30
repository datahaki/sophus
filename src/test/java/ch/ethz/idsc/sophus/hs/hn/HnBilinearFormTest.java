// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class HnBilinearFormTest extends TestCase {
  public void testNegative() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      StaticHelper.requirePoint(p);
      StaticHelper.requirePoint(q);
      Scalar pq = HnBilinearForm.between(p, q);
      assertTrue(Scalars.lessEquals(pq, RealScalar.ONE.negate()));
    }
  }

  public void testSimple() {
    Tensor J = DiagonalMatrix.of(1, 1, -1);
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Tolerance.CHOP.requireClose( //
          HnBilinearForm.between(p, q), //
          J.dot(p).dot(q));
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
