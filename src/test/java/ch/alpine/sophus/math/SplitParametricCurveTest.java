// code by jph
package ch.alpine.sophus.math;

import ch.alpine.sophus.api.Geodesic;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;
import junit.framework.TestCase;

public class SplitParametricCurveTest extends TestCase {
  public void testSimple() {
    Geodesic geodesicInterface = SplitParametricCurve.of(RnGeodesic.INSTANCE);
    ScalarTensorFunction scalarTensorFunction = geodesicInterface.curve(UnitVector.of(3, 0), UnitVector.of(3, 1));
    assertEquals(scalarTensorFunction.apply(RealScalar.ZERO), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.vector(0.5, 0.5, 0));
  }

  public void testNullFail() {
    AssertFail.of(() -> SplitParametricCurve.of(null));
  }
}
