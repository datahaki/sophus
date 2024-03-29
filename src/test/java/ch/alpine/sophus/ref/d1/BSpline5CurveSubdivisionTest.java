// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.clt.ClothoidBuilder;
import ch.alpine.sophus.crv.clt.ClothoidBuilders;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class BSpline5CurveSubdivisionTest {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  @Test
  void testCyclicMask() {
    CurveSubdivision curveSubdivision = new BSpline5CurveSubdivision(RnGroup.INSTANCE);
    Tensor tensor = curveSubdivision.cyclic(Tensors.vector(1, 0, 0, 0, 0, 0, 0));
    assertEquals(tensor, Tensors.fromString( //
        "{5/8, 15/32, 3/16, 1/32, 0, 0, 0, 0, 0, 0, 0, 1/32, 3/16, 15/32}"));
  }

  @Test
  void testString() {
    CurveSubdivision curveSubdivision = new BSpline5CurveSubdivision(RnGroup.INSTANCE);
    for (int length = 3; length < 7; ++length) {
      Tensor tensor = curveSubdivision.cyclic(UnitVector.of(length, 2));
      ExactTensorQ.require(tensor);
      assertEquals(Total.of(tensor), RealScalar.of(2));
    }
  }

  @Test
  void testEmpty() {
    Tensor curve = Tensors.vector();
    CurveSubdivision curveSubdivision = new BSpline5CurveSubdivision(RnGroup.INSTANCE);
    assertEquals(curveSubdivision.string(curve), Tensors.empty());
    assertEquals(curveSubdivision.cyclic(curve), Tensors.empty());
  }

  @Test
  void testSingleton() {
    Tensor singleton = Tensors.of(Tensors.vector(1, 2, 3));
    CurveSubdivision curveSubdivision = new BSpline5CurveSubdivision(CLOTHOID_BUILDER);
    assertEquals(curveSubdivision.cyclic(singleton), singleton);
    assertEquals(curveSubdivision.string(singleton), singleton);
  }

  @Test
  void testTerminal() {
    CurveSubdivision curveSubdivision = new BSpline5CurveSubdivision(RnGroup.INSTANCE);
    Clip clip = Clips.interval(1, 2);
    for (int length = 2; length < 7; ++length) {
      Tensor tensor = curveSubdivision.string(UnitVector.of(length, 0));
      ExactTensorQ.require(tensor);
      clip.requireInside(Total.ofVector(tensor));
    }
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> new BSpline5CurveSubdivision(null));
  }
}
