// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnExponentialTest extends TestCase {
  public void testExp() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 4; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, d));
      Tensor y = hnExponential.exp(v);
      HnMemberQ.INSTANCE.require(y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Tolerance.CHOP.requireClose(dxy, HnNorm.INSTANCE.norm(v));
    }
  }

  public void testExpZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 4; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, Array.zeros(d));
      assertEquals(v, Array.zeros(d + 1));
      Tensor y = hnExponential.exp(v);
      HnMemberQ.INSTANCE.require(y);
      Tolerance.CHOP.requireClose(x, y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Chop._04.requireClose(dxy, HnNorm.INSTANCE.norm(v));
    }
  }

  public void testLog() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 1; d < 4; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = hnExponential.log(y);
      new THnMemberQ(x).require(v);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Scalar vn1 = HnNorm.INSTANCE.norm(v);
      Chop._08.requireClose(dxy, vn1);
    }
  }

  public void testLogZero() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 1; d < 4; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = hnExponential.log(x);
      new THnMemberQ(x).require(v);
      Scalar dxy = HnMetric.INSTANCE.distance(x, x);
      Scalar vn1 = HnNorm.INSTANCE.norm(v);
      Chop._06.requireClose(dxy, vn1);
    }
  }
}
