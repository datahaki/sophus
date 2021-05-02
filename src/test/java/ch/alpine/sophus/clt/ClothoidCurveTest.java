// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.Unit;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class ClothoidCurveTest extends TestCase {
  private static final Unit METER = Unit.of("m");
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

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
      if (!Chop._03.isClose(r1, r2) || !Chop._03.isClose(r1, r3))
        ++fails;
    }
    assertTrue(fails < 10);
  }

  public void testDistinct() {
    Tensor p = Array.zeros(3);
    Tensor q = Tensors.vector(-3.7, 0.3, 3.142);
    Tensor m1 = Clothoid1.INSTANCE.curve(p, q).apply(RationalScalar.HALF);
    Tensor m2 = Clothoid2.INSTANCE.curve(p, q).apply(RationalScalar.HALF);
    Tensor m3 = CLOTHOID_BUILDER.curve(p, q).apply(RationalScalar.HALF);
    assertFalse(Chop._01.isClose(m1, m2));
    assertFalse(Chop._01.isClose(m1, m3));
    assertFalse(Chop._01.isClose(m2, m3));
  }
}