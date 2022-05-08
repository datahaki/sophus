// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;

class SplitParametricCurveTest {
  @Test
  public void testSimple() {
    GeodesicSpace geodesicInterface = SplitParametricCurve.of(RnGeodesic.INSTANCE);
    ScalarTensorFunction scalarTensorFunction = geodesicInterface.curve(UnitVector.of(3, 0), UnitVector.of(3, 1));
    assertEquals(scalarTensorFunction.apply(RealScalar.ZERO), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.vector(0.5, 0.5, 0));
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> SplitParametricCurve.of(null));
  }
}
