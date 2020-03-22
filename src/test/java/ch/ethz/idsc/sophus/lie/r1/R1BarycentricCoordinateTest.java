// code by jph
package ch.ethz.idsc.sophus.lie.r1;

import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Sort;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class R1BarycentricCoordinateTest extends TestCase {
  public void testSimple() {
    ScalarTensorFunction scalarTensorFunction = //
        R1BarycentricCoordinate.of(Tensors.vector(1, 2, 4), 1);
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(4)), UnitVector.of(3, 2));
    Tensor w1 = scalarTensorFunction.apply(RationalScalar.of(3, 2));
    ExactTensorQ.require(w1);
    assertEquals(w1, Tensors.fromString("{5/12, 5/8, -1/24}"));
    Tensor w2 = scalarTensorFunction.apply(RealScalar.of(2.0001));
    Chop._03.requireClose(w2, UnitVector.of(3, 1));
  }

  public void testQuantity() {
    ScalarTensorFunction scalarTensorFunction = //
        R1BarycentricCoordinate.of(Tensors.fromString("{1[m], 2[m], 4[m]}"), 1);
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
    for (int d = 0; d < 4; ++d) {
      ScalarTensorFunction scalarTensorFunction = R1BarycentricCoordinate.of(tensor, d);
      Scalar x = RandomVariate.of(distribution);
      Tensor weights = scalarTensorFunction.apply(x);
      Tensor tensor2 = RnBiinvariantMean.INSTANCE.mean(tensor, weights);
      Tolerance.CHOP.requireClose(x, tensor2);
    }
  }

  public void testUnorderedFail() {
    try {
      R1BarycentricCoordinate.of(Tensors.vector(2, 1, 3), 1);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNegativeFail() {
    try {
      R1BarycentricCoordinate.of(Tensors.vector(1, 2, 3), -2);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
