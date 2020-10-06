// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class ControlMidpointsTest extends TestCase {
  public void testCyclic() {
    CurveSubdivision curveSubdivision = ControlMidpoints.of(RnGeodesic.INSTANCE);
    assertEquals( //
        curveSubdivision.cyclic(Tensors.vector(1, 2, 3, 4, 5)), //
        Tensors.fromString("{3/2, 5/2, 7/2, 9/2, 3}"));
    assertEquals( //
        curveSubdivision.cyclic(Tensors.vector(10)), //
        Tensors.vector(10));
    assertEquals( //
        curveSubdivision.cyclic(Tensors.empty()), //
        Tensors.empty());
  }

  public void testString() {
    CurveSubdivision curveSubdivision = ControlMidpoints.of(RnGeodesic.INSTANCE);
    assertEquals( //
        curveSubdivision.string(Tensors.vector(1, 2, 3, 4, 5)), //
        Tensors.fromString("{3/2, 5/2, 7/2, 9/2}"));
    assertEquals( //
        curveSubdivision.string(Tensors.vector(10)), //
        Tensors.empty());
    assertEquals( //
        curveSubdivision.string(Tensors.empty()), //
        Tensors.empty());
  }
}
