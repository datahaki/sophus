// code by jph
package ch.alpine.sophus.itp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.sca.Chop;

public class BarycentricMetricInterpolationTest {
  @Test
  public void testSimpleBa() {
    ScalarTensorFunction scalarTensorFunction = //
        BarycentricMetricInterpolation.of(Tensors.vector(1, 2, 4), InversePowerVariogram.of(2));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(4)), UnitVector.of(3, 2));
    Tensor w1 = scalarTensorFunction.apply(RationalScalar.of(3, 2));
    ExactTensorQ.require(w1);
    assertEquals(w1, Tensors.fromString("{85/169, 335/676, 1/676}"));
    Tensor w2 = scalarTensorFunction.apply(RealScalar.of(2.0001));
    Chop._03.requireClose(w2, UnitVector.of(3, 1));
  }

  @Test
  public void testSimpleLa() {
    ScalarTensorFunction scalarTensorFunction = //
        BarycentricMetricInterpolation.la(Tensors.vector(1, 2, 4), InversePowerVariogram.of(2));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(4)), UnitVector.of(3, 2));
    Tensor w1 = scalarTensorFunction.apply(RationalScalar.of(3, 2));
    ExactTensorQ.require(w1);
    assertEquals(w1, Tensors.fromString("{60/119, 235/476, 1/476}"));
    Tensor w2 = scalarTensorFunction.apply(RealScalar.of(2.0001));
    Chop._03.requireClose(w2, UnitVector.of(3, 1));
  }
}
