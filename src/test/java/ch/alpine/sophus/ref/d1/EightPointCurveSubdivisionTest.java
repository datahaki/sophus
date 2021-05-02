// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class EightPointCurveSubdivisionTest extends TestCase {
  public void testSimple() {
    CurveSubdivision curveSubdivision = new EightPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor cyclic = curveSubdivision.cyclic(UnitVector.of(10, 5));
    assertEquals(Total.of(cyclic), RealScalar.of(2));
    ExactTensorQ.require(cyclic);
    Tensor result = Tensors.fromString( //
        "{0, 0, 0, -5/2048, 0, 49/2048, 0, -245/2048, 0, 1225/2048, 1, 1225/2048, 0, -245/2048, 0, 49/2048, 0, -5/2048, 0, 0}");
    assertEquals(cyclic, result);
  }

  public void testCircle() {
    CurveSubdivision curveSubdivision = new EightPointCurveSubdivision(RnGeodesic.INSTANCE);
    for (int n = 40; n < 60; n += 3)
      Chop._09.requireClose(curveSubdivision.cyclic(CirclePoints.of(n)), CirclePoints.of(n * 2));
  }

  public void testNullFail() {
    AssertFail.of(() -> new EightPointCurveSubdivision(null));
  }
}
