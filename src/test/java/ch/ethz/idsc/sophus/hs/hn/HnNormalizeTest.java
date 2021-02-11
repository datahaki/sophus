// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class HnNormalizeTest extends TestCase {
  public void testExp() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, d));
      v = HnNormalize.INSTANCE.apply(v);
      Tolerance.CHOP.requireClose(HnNorm.INSTANCE.ofVector(v), RealScalar.ONE);
    }
  }
}
