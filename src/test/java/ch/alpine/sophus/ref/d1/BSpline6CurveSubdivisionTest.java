// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.clt.ClothoidBuilder;
import ch.alpine.sophus.crv.clt.ClothoidBuilders;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.ExactTensorQ;

class BSpline6CurveSubdivisionTest {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  @Test
  public void testSimple() {
    CurveSubdivision curveSubdivision = //
        BSpline6CurveSubdivision.of(RnGeodesic.INSTANCE);
    Tensor tensor = curveSubdivision.cyclic(UnitVector.of(5, 0));
    assertEquals(tensor, //
        Tensors.fromString("{35/64, 21/64, 7/64, 1/64, 0, 0, 1/64, 7/64, 21/64, 35/64}"));
    ExactTensorQ.require(tensor);
  }

  @Test
  public void testEmpty() {
    Tensor curve = Tensors.vector();
    CurveSubdivision curveSubdivision = BSpline6CurveSubdivision.of(RnGeodesic.INSTANCE);
    assertEquals(curveSubdivision.cyclic(curve), Tensors.empty());
  }

  @Test
  public void testSingleton() {
    Tensor singleton = Tensors.of(Tensors.vector(1, 2, 3));
    CurveSubdivision curveSubdivision = BSpline6CurveSubdivision.of(CLOTHOID_BUILDER);
    assertEquals(curveSubdivision.cyclic(singleton), singleton);
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> BSpline6CurveSubdivision.of(null));
  }
}
