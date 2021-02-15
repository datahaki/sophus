// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.util.Random;

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
      new THnMemberQ(x).require(v);
      Tensor y = hnExponential.exp(v);
      HnMemberQ.INSTANCE.require(y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Tolerance.CHOP.requireClose(dxy, HnVectorNorm.of(v));
    }
  }

  public void testExpZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 4; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, Array.zeros(d));
      new THnMemberQ(x).require(v);
      assertEquals(v, Array.zeros(d + 1));
      Tensor y = hnExponential.exp(v);
      HnMemberQ.INSTANCE.require(y);
      Tolerance.CHOP.requireClose(x, y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Chop._04.requireClose(dxy, HnVectorNorm.of(v));
    }
  }

  public void testLog() {
    Random random = new Random(1);
    Distribution distribution = NormalDistribution.of(0, 5);
    for (int d = 1; d < 4; ++d)
      for (int count = 0; count < 100; ++count) {
        Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, d));
        Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, d));
        Tensor z = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, d));
        HnExponential hnExponential = new HnExponential(x);
        Tensor vy = hnExponential.log(y);
        Tensor vz = hnExponential.log(z);
        THnMemberQ tHnMemberQ = new THnMemberQ(x);
        tHnMemberQ.require(vy);
        tHnMemberQ.require(vz);
        Tensor v = vy.add(vz);
        tHnMemberQ.require(v);
        Tensor a = hnExponential.exp(v);
        HnMemberQ.INSTANCE.require(a);
        Scalar dxy = HnMetric.INSTANCE.distance(x, y);
        Scalar vn1 = HnVectorNorm.of(vy);
        Chop._06.requireClose(dxy, vn1);
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
      Scalar vn1 = HnVectorNorm.of(v);
      Chop._06.requireClose(dxy, vn1);
    }
  }
}
