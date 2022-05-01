// code by jph
package ch.alpine.sophus.itp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Sort;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Chop;

public class BarycentricRationalInterpolationTest {
  @Test
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

  @Test
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

  @Test
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

  @Test
  public void testLinearReproduction() {
    Random random = new Random(3);
    Distribution distribution = NormalDistribution.standard();
    Tensor knots = Sort.of(RandomVariate.of(distribution, random, 10));
    for (int d = 0; d < 4; ++d) {
      ScalarTensorFunction scalarTensorFunction = BarycentricRationalInterpolation.of(knots, d);
      Scalar x = RandomVariate.of(distribution, random);
      Tensor weights = scalarTensorFunction.apply(x);
      Tensor tensor2 = RnBiinvariantMean.INSTANCE.mean(knots, weights);
      Chop._08.requireClose(x, tensor2);
    }
  }

  @Test
  public void testUnorderedFail() {
    assertThrows(Exception.class, () -> BarycentricRationalInterpolation.of(Tensors.vector(2, 1, 3), 1));
  }

  @Test
  public void testNegativeFail() {
    BarycentricRationalInterpolation.of(Tensors.vector(1, 2, 3), 10);
    assertThrows(Exception.class, () -> BarycentricRationalInterpolation.of(Tensors.vector(1, 2, 3), -2));
  }
}
