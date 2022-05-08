// code by ob
package ch.alpine.sophus.crv.spline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.hs.r2.Se2Parametric;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.lie.se2.Se2Geodesic;
import ch.alpine.sophus.math.win.KnotSpacing;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class GeodesicCatmullRomTest {
  @Test
  public void testUniformInterpolatory() throws ClassNotFoundException, IOException {
    Tensor control = RandomVariate.of(UniformDistribution.unit(), 5, 3);
    TensorUnaryOperator centripedalKnotSpacing = KnotSpacing.uniform();
    Tensor knots = centripedalKnotSpacing.apply(control);
    GeodesicCatmullRom geodesicCatmullRom = //
        Serialization.copy(GeodesicCatmullRom.of(Se2Geodesic.INSTANCE, knots, control));
    // ---
    Tensor actual1 = geodesicCatmullRom.apply(RealScalar.of(1));
    Tensor expected1 = control.get(1);
    // ----
    Tensor actual2 = geodesicCatmullRom.apply(RealScalar.of(2));
    Tensor expected2 = control.get(2);
    // ----
    Chop._10.requireClose(actual2, expected2);
    Chop._10.requireClose(actual1, expected1);
    assertEquals(geodesicCatmullRom.control(), control);
  }

  @Test
  public void testCentripetalInterpolatory() {
    GeodesicSpace geodesicInterface = Se2Geodesic.INSTANCE;
    Tensor control = Tensors.empty();
    for (int index = 0; index < 5; index++)
      control.append(Tensors.vector(Math.random(), Math.random(), Math.random()));
    TensorUnaryOperator centripedalKnotSpacing = //
        KnotSpacing.centripetal(Se2Parametric.INSTANCE, RealScalar.of(Math.random()));
    Tensor knots = centripedalKnotSpacing.apply(control);
    GeodesicCatmullRom geodesicCatmullRom = GeodesicCatmullRom.of(geodesicInterface, knots, control);
    // ---
    Chop._10.requireClose(geodesicCatmullRom.apply(geodesicCatmullRom.knots().Get(1)), control.get(1));
    Chop._10.requireClose(geodesicCatmullRom.apply(geodesicCatmullRom.knots().Get(2)), control.get(2));
  }

  @Test
  public void testLengthFail() {
    Tensor control = RandomVariate.of(UniformDistribution.unit(), 3, 7);
    Tensor knots = KnotSpacing.uniform().apply(control);
    assertThrows(Exception.class, () -> GeodesicCatmullRom.of(RnGeodesic.INSTANCE, knots, control));
  }

  @Test
  public void testKnotsInconsistentFail() {
    Tensor control = RandomVariate.of(UniformDistribution.unit(), 5, 7);
    Tensor knots = KnotSpacing.uniform().apply(control);
    GeodesicCatmullRom.of(RnGeodesic.INSTANCE, knots, control);
    assertThrows(Exception.class, () -> GeodesicCatmullRom.of(RnGeodesic.INSTANCE, knots.extract(0, knots.length() - 1), control));
  }
}
