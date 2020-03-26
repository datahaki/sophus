// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2ClothoidTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(-8, 8);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid clothoid = Se2Clothoid.of(p, q);
      Chop._07.requireZero(So2.MOD.apply(clothoid.angle(RealScalar.ZERO).subtract(p.get(2))));
      Chop._07.requireZero(So2.MOD.apply(clothoid.angle(RealScalar.ONE).subtract(q.get(2))));
    }
  }
}
