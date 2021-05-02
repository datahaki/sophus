// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.red.Total;
import junit.framework.TestCase;

public class DualC2FourPointCurveSubdivisionTest extends TestCase {
  public void testSimple() {
    CurveSubdivision curveSubdivision = DualC2FourPointCurveSubdivision.cubic(RnGeodesic.INSTANCE);
    Tensor cyclic = curveSubdivision.cyclic(UnitVector.of(10, 1));
    assertEquals(Total.of(cyclic), RealScalar.of(2));
  }

  public void testSpecs() {
    CurveSubdivision curveSubdivision = DualC2FourPointCurveSubdivision.cubic(RnGeodesic.INSTANCE);
    Tensor cyclic = curveSubdivision.cyclic(UnitVector.of(6, 3));
    assertEquals(cyclic, Tensors.fromString("{0, 0, -5/128, -7/128, 35/128, 105/128, 105/128, 35/128, -7/128, -5/128, 0, 0}"));
  }

  public void testTightest() {
    CurveSubdivision curveSubdivision = DualC2FourPointCurveSubdivision.tightest(RnGeodesic.INSTANCE);
    curveSubdivision.cyclic(UnitVector.of(4, 2));
  }

  public void testNullFail() {
    AssertFail.of(() -> DualC2FourPointCurveSubdivision.cubic(null));
    AssertFail.of(() -> DualC2FourPointCurveSubdivision.tightest(null));
  }
}
