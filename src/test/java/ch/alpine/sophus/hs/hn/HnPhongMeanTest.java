// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnPhongMeanTest extends TestCase {
  public void testMidpoint() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 1; d < 6; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor m1 = HnGeodesic.INSTANCE.midpoint(x, y);
      HnMemberQ.INSTANCE.require(m1);
      Tensor m2 = HnGeodesic.INSTANCE.curve(x, y).apply(RationalScalar.HALF);
      HnMemberQ.INSTANCE.require(m2);
      Chop._08.requireClose(m1, m2);
      Tensor mp = HnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
      Chop._08.requireClose(m1, mp);
    }
  }

  public void testAffineFail() {
    Tensor x = HnWeierstrassCoordinate.toPoint(Tensors.vector(0, 0));
    Tensor y = HnWeierstrassCoordinate.toPoint(Tensors.vector(1, 0));
    HnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
    // AssertFail.of(() -> HnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.6, 0.5)));
  }
}
