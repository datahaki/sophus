// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class HnNormSquaredTest extends TestCase {
  public void testNegative() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      StaticHelper.requirePoint(p);
      StaticHelper.requirePoint(q);
      Tensor dif = p.subtract(q);
      Scalar dd = HnNormSquared.INSTANCE.norm(dif);
      Sign.requirePositiveOrZero(dd);
    }
  }
}
