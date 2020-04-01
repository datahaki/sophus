// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.bn.BnCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class BnCoordinateTest extends TestCase {
  public void testSimple() {
    Tensor x = Tensors.vector(-0.3, 0.5);
    Tensor hn = BnCoordinate.bnToHn(x);
    StaticHelper.requirePoint(hn);
  }

  public void testMore() {
    Tensor x = Tensors.vector(0.2, 0);
    Tensor hn = BnCoordinate.bnToHn(x);
    StaticHelper.requirePoint(hn);
    Tolerance.CHOP.requireClose(hn, Tensors.vector(0.41666666666666674, 0, 1.0833333333333335));
  }

  public void testRandom() {
    Distribution distribution = UniformDistribution.of(-0.5, 0.5);
    for (int count = 0; count < 10; ++count) {
      Tensor b2 = RandomVariate.of(distribution, 2);
      Tensor h2 = BnCoordinate.bnToHn(b2);
      StaticHelper.requirePoint(h2);
      Tolerance.CHOP.requireClose(BnCoordinate.hnToBn(h2), b2);
    }
  }
}
