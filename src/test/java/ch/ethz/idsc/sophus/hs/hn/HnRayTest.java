// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class HnRayTest extends TestCase {
  public void testExp() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xn = RandomVariate.of(distribution, 3);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      HnRay hnRay = new HnRay(x);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, 3));
      v = HnNormalize.INSTANCE.apply(v);
      Tolerance.CHOP.requireClose(HnNorm.INSTANCE.norm(v), RealScalar.ONE);
      Tolerance.CHOP.requireZero(HnBilinearForm.between(x, v));
      Tensor y = hnRay.shoot(v, RandomVariate.of(distribution));
      StaticHelper.requirePoint(y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      System.out.println(dxy);
    }
  }

  public void testLog() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      // Tensor xn = ;
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, 3));
      HnExponential hnExp = new HnExponential(x);
      Tensor v = hnExp.log(y);
      // System.out.println(v);
      // StaticHelper.requireTangent(x, v); // FIXME
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Scalar vn1 = HnNormSquared.INSTANCE.norm(v);
      // Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, 3));
      // Tensor y = hnExp.exp(v);
      // StaticHelper.requirePoint(y); // FIXME
      // System.out.println("---");
      // System.out.println(dxy);
      // System.out.println(vn1);
      // Tensor w = hnExp.log(y);
    }
  }
}
