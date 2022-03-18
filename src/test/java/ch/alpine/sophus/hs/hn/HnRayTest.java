// code by jph
package ch.alpine.sophus.hs.hn;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Sign;

public class HnRayTest {
  @Test
  public void testExp() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      HnRay hnRay = new HnRay(x);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, d));
      v = HnVectorNorm.NORMALIZE.apply(v);
      Tolerance.CHOP.requireClose(HnVectorNorm.of(v), RealScalar.ONE);
      Tolerance.CHOP.requireZero(LBilinearForm.between(x, v));
      Tensor y = hnRay.shoot(v, RandomVariate.of(distribution));
      HnMemberQ.INSTANCE.require(y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Sign.requirePositiveOrZero(dxy);
    }
  }
}
