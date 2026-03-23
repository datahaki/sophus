// code by jph
package ch.alpine.sophus.clt;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class ClothoidBuilderImplTest {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(-8, 8);
    ClothoidBuilder clothoidInterface = Serialization.copy(CLOTHOID_BUILDER);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid clothoid = clothoidInterface.curve(p, q);
      Chop._07.requireZero(So2.MOD.apply(clothoid.apply(RealScalar.ZERO).Get(2).subtract(p.Get(2))));
      Chop._07.requireZero(So2.MOD.apply(clothoid.apply(RealScalar.ONE).Get(2).subtract(q.Get(2))));
    }
  }

  @Test
  void testErf() {
    ScalarTensorFunction scalarTensorFunction = CLOTHOID_BUILDER.curve(Tensors.vector(1, 2, 3), Array.zeros(3));
    assertInstanceOf(Clothoid.class, scalarTensorFunction);
  }
}
