// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnPhongMeanTest extends TestCase {
  private static final MemberQ MEMBER_Q = HnMemberQ.of(Tolerance.CHOP);

  public void testMidpoint() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 1; d < 6; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor m1 = HnGeodesic.INSTANCE.midpoint(x, y);
      MEMBER_Q.requirePoint(m1);
      Tensor m2 = HnGeodesic.INSTANCE.curve(x, y).apply(RationalScalar.HALF);
      MEMBER_Q.requirePoint(m2);
      Chop._08.requireClose(m1, m2);
      Tensor mp = HnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
      Chop._08.requireClose(m1, mp);
    }
  }

  public void testAffineFail() {
    Tensor x = HnWeierstrassCoordinate.toPoint(Tensors.vector(0, 0));
    Tensor y = HnWeierstrassCoordinate.toPoint(Tensors.vector(1, 0));
    HnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
    try {
      HnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.6, 0.5));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
