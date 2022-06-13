// code by jph
package ch.alpine.sophus.hs.hn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Imag;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Sqrt;

class HnMetricTest {
  @Test
  void testZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Scalar dxy = HnMetric.INSTANCE.distance(x, x);
      Chop._06.requireZero(dxy);
      assertTrue(Scalars.isZero(Imag.FUNCTION.apply(dxy)));
    }
  }

  @Test
  void testPositive() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Sign.requirePositiveOrZero(dxy);
    }
  }

  @Test
  void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, d));
      assertEquals(v.length(), d + 1);
      Scalar vn1 = LBilinearForm.normSquared(v);
      Sign.requirePositiveOrZero(vn1);
    }
  }

  @Test
  void testConsistent() {
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
