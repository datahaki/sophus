// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import junit.framework.TestCase;

public class SplitParametricCurveTest extends TestCase {
  public void testSimple() {
    GeodesicInterface geodesicInterface = SplitParametricCurve.of(RnGeodesic.INSTANCE);
    ScalarTensorFunction scalarTensorFunction = geodesicInterface.curve(UnitVector.of(3, 0), UnitVector.of(3, 1));
    assertEquals(scalarTensorFunction.apply(RealScalar.ZERO), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.vector(0.5, 0.5, 0));
  }

  public void testNullFail() {
    try {
      SplitParametricCurve.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
