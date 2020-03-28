// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class HnWeierstrassCoordinateTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xn = RandomVariate.of(distribution, 3);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      StaticHelper.requirePoint(x);
      Tensor vn = RandomVariate.of(distribution, 3);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, vn);
      StaticHelper.requireTangent(x, v);
    }
  }
}
