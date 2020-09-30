// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class HnProjectionTest extends TestCase {
  private static final HsMemberQ HS_MEMBER_Q = HnMemberQ.of(Tolerance.CHOP);

  public void testSimple() {
    Tensor x = HnWeierstrassCoordinate.toPoint(Tensors.vector(1, 2, 3));
    Tolerance.CHOP.requireClose(x, HnProjection.INSTANCE.apply(x));
  }

  public void testRandom() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 4; ++d) {
      Tensor xd = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      HS_MEMBER_Q.requirePoint(xd);
      xd.set(RealScalar.ONE::add, d);
      Tensor x = HnProjection.INSTANCE.apply(xd);
      HS_MEMBER_Q.requirePoint(x);
    }
  }
}
