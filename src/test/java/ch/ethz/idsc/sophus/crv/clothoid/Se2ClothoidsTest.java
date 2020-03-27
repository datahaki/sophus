// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2ClothoidsTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(-8, 8);
    ClothoidInterface clothoidInterface = Serialization.copy(Se2Clothoids.INSTANCE);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid clothoid = clothoidInterface.curve(p, q);
      Chop._07.requireZero(So2.MOD.apply(clothoid.apply(RealScalar.ZERO).Get(2).subtract(p.get(2))));
      Chop._07.requireZero(So2.MOD.apply(clothoid.apply(RealScalar.ONE).Get(2).subtract(q.get(2))));
    }
  }
}
