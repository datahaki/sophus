// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Sign;
import ch.ethz.idsc.tensor.sca.Sqrt;
import junit.framework.TestCase;

public class HnMetricTest extends TestCase {
  public void testZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Scalar dxy = HnMetric.INSTANCE.distance(x, x);
      Chop._06.requireZero(dxy);
      assertTrue(Scalars.isZero(Imag.FUNCTION.apply(dxy)));
    }
  }

  public void testPositive() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Sign.requirePositiveOrZero(dxy);
    }
  }

  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, d));
      assertEquals(v.length(), d + 1);
      Scalar vn1 = LBilinearForm.normSquared(v);
      Sign.requirePositiveOrZero(vn1);
    }
  }

  public void testConsistent() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Scalar distance = HnMetric.INSTANCE.distance(p, q);
      Tensor v = HnManifold.INSTANCE.exponential(p).log(q);
      new THnMemberQ(p).require(v);
      Scalar vn1 = Sqrt.FUNCTION.apply(LBilinearForm.normSquared(v));
      Chop._10.requireClose(distance, vn1);
      Scalar vn2 = HnVectorNorm.of(v);
      Chop._10.requireClose(vn2, vn1);
    }
  }
}
