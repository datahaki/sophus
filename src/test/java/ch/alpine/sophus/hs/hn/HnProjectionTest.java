// code by jph
package ch.alpine.sophus.hs.hn;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class HnProjectionTest {
  @Test
  void testSimple() {
    Tensor x = HnWeierstrassCoordinate.toPoint(Tensors.vector(1, 2, 3));
    Tolerance.CHOP.requireClose(x, HnProjection.INSTANCE.apply(x));
  }

  @Test
  void testRandom() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 4; ++d) {
      Tensor xd = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      HnMemberQ.INSTANCE.require(xd);
      xd.set(RealScalar.ONE::add, d);
      Tensor x = HnProjection.INSTANCE.apply(xd);
      HnMemberQ.INSTANCE.require(x);
    }
  }
}
