// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class BarycentricMetricInterpolationTest extends TestCase {
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
