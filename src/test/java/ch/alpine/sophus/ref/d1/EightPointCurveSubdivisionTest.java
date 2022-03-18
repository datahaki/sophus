// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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

public class EightPointCurveSubdivisionTest {
  @Test
  public void testSimple() {
    CurveSubdivision curveSubdivision = new EightPointCurveSubdivision(RnGeodesic.INSTANCE);
    Tensor cyclic = curveSubdivision.cyclic(UnitVector.of(10, 5));
    assertEquals(Total.of(cyclic), RealScalar.of(2));
    ExactTensorQ.require(cyclic);
    Tensor result = Tensors.fromString( //
        "{0, 0, 0, -5/2048, 0, 49/2048, 0, -245/2048, 0, 1225/2048, 1, 1225/2048, 0, -245/2048, 0, 49/2048, 0, -5/2048, 0, 0}");
    assertEquals(cyclic, result);
  }

  @Test
  public void testCircle() {
    CurveSubdivision curveSubdivision = new EightPointCurveSubdivision(RnGeodesic.INSTANCE);
    for (int n = 40; n < 60; n += 3)
      Chop._09.requireClose(curveSubdivision.cyclic(CirclePoints.of(n)), CirclePoints.of(n * 2));
  }

  @Test
  public void testNullFail() {
    AssertFail.of(() -> new EightPointCurveSubdivision(null));
  }
}
