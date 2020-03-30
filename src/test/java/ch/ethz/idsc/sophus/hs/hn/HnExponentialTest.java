// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class HnExponentialTest extends TestCase {
  public void testExp() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xn = RandomVariate.of(distribution, 3);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, 3));
      Tensor y = hnExponential.exp(v);
      StaticHelper.requirePoint(y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Tolerance.CHOP.requireClose(dxy, HnNorm.INSTANCE.norm(v));
    }
  }

  public void testLog() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      // Tensor xn = ;
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = hnExponential.log(y);
      StaticHelper.requireTangent(x, v);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Scalar vn1 = HnNorm.INSTANCE.norm(v);
      Tolerance.CHOP.requireClose(dxy, vn1);
    }
  }
}
