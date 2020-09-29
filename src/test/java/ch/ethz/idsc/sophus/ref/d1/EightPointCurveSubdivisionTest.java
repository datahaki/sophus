// code by jph
package ch.ethz.idsc.sophus.ref.d1;

import ch.ethz.idsc.sophus.clt.ClothoidBuilder;
import ch.ethz.idsc.sophus.clt.ClothoidBuilders;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.lie.so2.CirclePoints;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class EightPointCurveSubdivisionTest extends TestCase {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

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

  public void testStringNullFail() {
    CurveSubdivision curveSubdivision = new EightPointCurveSubdivision(CLOTHOID_BUILDER);
    try {
      curveSubdivision.string(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}