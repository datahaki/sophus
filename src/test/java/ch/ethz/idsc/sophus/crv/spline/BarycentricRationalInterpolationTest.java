// code by jph
package ch.ethz.idsc.sophus.crv.spline;

import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Sort;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class BarycentricRationalInterpolationTest extends TestCase {
  public void testSimple() {
    ScalarTensorFunction scalarTensorFunction = //
        BarycentricRationalInterpolation.of(Tensors.vector(1, 2, 4), 1);
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(4)), UnitVector.of(3, 2));
    Tensor w1 = scalarTensorFunction.apply(RationalScalar.of(3, 2));
    ExactTensorQ.require(w1);
    assertEquals(w1, Tensors.fromString("{5/12, 5/8, -1/24}"));
    Tensor w2 = scalarTensorFunction.apply(RealScalar.of(2.0001));
    Chop._03.requireClose(w2, UnitVector.of(3, 1));
  }

  public void testDegrees() {
    for (int d = 0; d < 5; ++d) {
      ScalarTensorFunction scalarTensorFunction = //
          BarycentricRationalInterpolation.of(Tensors.vector(1, 2, 4), d);
      assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), UnitVector.of(3, 0));
      assertEquals(scalarTensorFunction.apply(RealScalar.of(4)), UnitVector.of(3, 2));
      Tensor w1 = scalarTensorFunction.apply(RationalScalar.of(3, 2));
      ExactTensorQ.require(w1);
      Tensor w2 = scalarTensorFunction.apply(RealScalar.of(2.0001));
      Chop._03.requireClose(w2, UnitVector.of(3, 1));
    }
  }

  public void testQuantity() {
    ScalarTensorFunction scalarTensorFunction = //
        BarycentricRationalInterpolation.of(Tensors.fromString("{1[m], 2[m], 4[m]}"), 1);
    assertEquals(scalarTensorFunction.apply(Quantity.of(1, "m")), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(Quantity.of(4, "m")), UnitVector.of(3, 2));
    Tensor w1 = scalarTensorFunction.apply(Quantity.of(RationalScalar.of(3, 2), "m"));
    ExactTensorQ.require(w1);
    assertEquals(w1, Tensors.fromString("{5/12, 5/8, -1/24}"));
    Tensor w2 = scalarTensorFunction.apply(Quantity.of(2.0001, "m"));
    Chop._03.requireClose(w2, UnitVector.of(3, 1));
  }

  public void testLinearReproduction() {
    Distribution distribution = NormalDistribution.standard();
    Tensor tensor = Sort.of(RandomVariate.of(distribution, 10));
    int fails = 0;
    for (int d = 0; d < 4; ++d)
      try {
        ScalarTensorFunction scalarTensorFunction = BarycentricRationalInterpolation.of(tensor, d);
        Scalar x = RandomVariate.of(distribution);
        Tensor weights = scalarTensorFunction.apply(x);
        Tensor tensor2 = RnBiinvariantMean.INSTANCE.mean(tensor, weights);
        Chop._08.requireClose(x, tensor2);
      } catch (Exception exception) {
        ++fails;
      }
    assertTrue(fails <= 2);
  }

  public void testUnorderedFail() {
    AssertFail.of(() -> BarycentricRationalInterpolation.of(Tensors.vector(2, 1, 3), 1));
  }

  public void testNegativeFail() {
    BarycentricRationalInterpolation.of(Tensors.vector(1, 2, 3), 10);
    AssertFail.of(() -> BarycentricRationalInterpolation.of(Tensors.vector(1, 2, 3), -2));
  }
}
