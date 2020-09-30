// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class HnNormSquaredTest extends TestCase {
  private static final HsMemberQ HS_MEMBER_Q = HnMemberQ.of(Tolerance.CHOP);

  public void testNegative() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      HS_MEMBER_Q.requirePoint(p);
      HS_MEMBER_Q.requirePoint(q);
      Tensor dif = p.subtract(q);
      Scalar dd = HnNormSquared.INSTANCE.norm(dif);
      Sign.requirePositiveOrZero(dd);
    }
  }
}
