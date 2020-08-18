// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.Unit;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ClothoidCurveTest extends TestCase {
  private static final Unit METER = Unit.of("m");

  public static Tensor metric(Tensor vector) {
    return Tensors.of( //
        Quantity.of(vector.Get(0), METER), //
        Quantity.of(vector.Get(1), METER), //
        vector.Get(2));
  }

  public void testComparison() {
    Distribution distribution = NormalDistribution.of(0, 0.002);
    int fails = 0;
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Scalar lambda = RandomVariate.of(distribution);
      Tensor r1 = new ClothoidCurve1(p, q).apply(lambda);
      Tensor r2 = new ClothoidCurve2(p, q).apply(lambda);
      Tensor r3 = new ClothoidCurve3(p, q).apply(lambda);
      if (!Chop._03.close(r1, r2) || !Chop._03.close(r1, r3))
        ++fails;
    }
    assertTrue(fails < 10);
  }

  public void testDistinct() {
    Tensor p = Array.zeros(3);
    Tensor q = Tensors.vector(-3.7, 0.3, 3.142);
    Tensor m1 = Clothoid1.INSTANCE.curve(p, q).apply(RationalScalar.HALF);
    Tensor m2 = Clothoid2.INSTANCE.curve(p, q).apply(RationalScalar.HALF);
    Tensor m3 = Se2ClothoidBuilder.INSTANCE.curve(p, q).apply(RationalScalar.HALF);
    assertFalse(Chop._01.close(m1, m2));
    assertFalse(Chop._01.close(m1, m3));
    assertFalse(Chop._01.close(m2, m3));
  }
}
