// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.sophus.hs.bn.BnCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class HnWeierstrassCoordinateTest extends TestCase {
  private static final MemberQ MEMBER_Q = HnMemberQ.of(Tolerance.CHOP);

  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      MEMBER_Q.requirePoint(x);
      Tensor vn = RandomVariate.of(distribution, d);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, vn);
      MEMBER_Q.requireTangent(x, v);
    }
  }

  public void testBn() {
    Tensor x = Tensors.vector(-0.3, 0.5);
    Tensor hn = BnCoordinate.bnToHn(x);
    MEMBER_Q.requirePoint(hn);
  }

  public void testMore() {
    Tensor x = Tensors.vector(0.2, 0);
    Tensor hn = BnCoordinate.bnToHn(x);
    MEMBER_Q.requirePoint(hn);
    Tolerance.CHOP.requireClose(hn, Tensors.vector(0.41666666666666674, 0, 1.0833333333333335));
  }

  public void testRandom() {
    Distribution distribution = UniformDistribution.of(-0.5, 0.5);
    for (int count = 0; count < 10; ++count) {
      Tensor b2 = RandomVariate.of(distribution, 2);
      Tensor h2 = BnCoordinate.bnToHn(b2);
      MEMBER_Q.requirePoint(h2);
      Tolerance.CHOP.requireClose(BnCoordinate.hnToBn(h2), b2);
    }
  }
}
