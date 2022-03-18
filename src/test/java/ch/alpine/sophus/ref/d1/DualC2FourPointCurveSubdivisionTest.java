// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.red.Total;

public class DualC2FourPointCurveSubdivisionTest {
  @Test
  public void testSimple() {
    CurveSubdivision curveSubdivision = DualC2FourPointCurveSubdivision.cubic(RnGeodesic.INSTANCE);
    Tensor cyclic = curveSubdivision.cyclic(UnitVector.of(10, 1));
    assertEquals(Total.of(cyclic), RealScalar.of(2));
  }

  @Test
  public void testSpecs() {
    CurveSubdivision curveSubdivision = DualC2FourPointCurveSubdivision.cubic(RnGeodesic.INSTANCE);
    Tensor cyclic = curveSubdivision.cyclic(UnitVector.of(6, 3));
    assertEquals(cyclic, Tensors.fromString("{0, 0, -5/128, -7/128, 35/128, 105/128, 105/128, 35/128, -7/128, -5/128, 0, 0}"));
  }

  @Test
  public void testTightest() {
    CurveSubdivision curveSubdivision = DualC2FourPointCurveSubdivision.tightest(RnGeodesic.INSTANCE);
    curveSubdivision.cyclic(UnitVector.of(4, 2));
  }

  @Test
  public void testNullFail() {
    AssertFail.of(() -> DualC2FourPointCurveSubdivision.cubic(null));
    AssertFail.of(() -> DualC2FourPointCurveSubdivision.tightest(null));
  }
}
