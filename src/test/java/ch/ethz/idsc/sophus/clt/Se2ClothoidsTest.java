// code by jph
package ch.ethz.idsc.sophus.clt;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2ClothoidsTest extends TestCase {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(-8, 8);
    ClothoidBuilder clothoidInterface = Serialization.copy(CLOTHOID_BUILDER);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid clothoid = clothoidInterface.curve(p, q);
      Chop._07.requireZero(So2.MOD.apply(clothoid.apply(RealScalar.ZERO).Get(2).subtract(p.get(2))));
      Chop._07.requireZero(So2.MOD.apply(clothoid.apply(RealScalar.ONE).Get(2).subtract(q.get(2))));
    }
  }

  public void testErf() {
    ScalarTensorFunction scalarTensorFunction = CLOTHOID_BUILDER.curve(Tensors.vector(1, 2, 3), Array.zeros(3));
    assertTrue(scalarTensorFunction instanceof Clothoid);
  }
}
