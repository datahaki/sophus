// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class HnRayTest extends TestCase {
  private static final MemberQ MEMBER_Q = HnMemberQ.of(Tolerance.CHOP);

  public void testExp() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      HnRay hnRay = new HnRay(x);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, d));
      v = HnNormalize.INSTANCE.apply(v);
      Tolerance.CHOP.requireClose(HnNorm.INSTANCE.norm(v), RealScalar.ONE);
      Tolerance.CHOP.requireZero(HnBilinearForm.between(x, v));
      Tensor y = hnRay.shoot(v, RandomVariate.of(distribution));
      MEMBER_Q.requirePoint(y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Sign.requirePositiveOrZero(dxy);
    }
  }
}
