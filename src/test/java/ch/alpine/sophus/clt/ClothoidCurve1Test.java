// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class ClothoidCurve1Test extends TestCase {
  public void testQuantity() {
    Tensor p1 = Tensors.fromString("{2[m], 3[m], 1}");
    Tensor q1 = Tensors.fromString("{4[m], 7[m], 2}");
    Tensor r1 = new ClothoidCurve1(p1, q1).apply(RationalScalar.HALF);
    Tensor p2 = Tensors.fromString("{2, 3, 1}");
    Tensor q2 = Tensors.fromString("{4, 7, 2}");
    Tensor r2 = ComplexClothoidCurve.INSTANCE.split(p2, q2, RationalScalar.HALF);
    Chop._01.requireClose(r1, ClothoidCurveTest.metric(r2));
  }

  public void testPreserve() {
    Distribution distribution = NormalDistribution.of(0, 0.001);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Scalar lambda = RandomVariate.of(distribution);
      Tensor r1 = new ClothoidCurve1( //
          ClothoidCurveTest.metric(p), //
          ClothoidCurveTest.metric(q)).apply(lambda);
      Tensor r2 = ComplexClothoidCurve.INSTANCE.split(p, q, lambda);
      Chop._03.requireClose(r1, ClothoidCurveTest.metric(r2));
    }
  }
}
