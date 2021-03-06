// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.IOException;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class HnWeierstrassCoordinateTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      assertEquals(x.length(), d + 1);
      HnMemberQ.INSTANCE.require(x);
      Tensor vn = RandomVariate.of(distribution, d);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, vn);
      Serialization.copy(new THnMemberQ(x)).require(v);
      assertEquals(v.length(), d + 1);
    }
  }

  public void testBn() {
    Tensor x = Tensors.vector(-0.3, 0.5);
    Tensor hn = BnCoordinate.bnToHn(x);
    HnMemberQ.INSTANCE.require(hn);
  }

  public void testMore() {
    Tensor x = Tensors.vector(0.2, 0);
    Tensor hn = BnCoordinate.bnToHn(x);
    HnMemberQ.INSTANCE.require(hn);
    Tolerance.CHOP.requireClose(hn, Tensors.vector(0.41666666666666674, 0, 1.0833333333333335));
  }

  public void testRandom() {
    Distribution distribution = UniformDistribution.of(-0.5, 0.5);
    for (int count = 0; count < 10; ++count) {
      Tensor b2 = RandomVariate.of(distribution, 2);
      Tensor h2 = BnCoordinate.bnToHn(b2);
      HnMemberQ.INSTANCE.require(h2);
      Tolerance.CHOP.requireClose(BnCoordinate.hnToBn(h2), b2);
    }
  }
}
