// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
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
