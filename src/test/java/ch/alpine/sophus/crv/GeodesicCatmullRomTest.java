// code by ob
package ch.alpine.sophus.crv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.r2.Se2Parametric;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
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
  void testUniformInterpolatory() throws ClassNotFoundException, IOException {
    Tensor control = RandomVariate.of(UniformDistribution.unit(), 5, 3);
    TensorUnaryOperator centripedalKnotSpacing = KnotSpacing.uniform();
    Tensor knots = centripedalKnotSpacing.apply(control);
    GeodesicCatmullRom geodesicCatmullRom = //
        Serialization.copy(GeodesicCatmullRom.of(Se2Group.INSTANCE, knots, control));
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
  void testCentripetalInterpolatory() {
    GeodesicSpace geodesicSpace = Se2Group.INSTANCE;
    Tensor control = Tensors.empty();
    for (int index = 0; index < 5; index++)
      control.append(Tensors.vector(Math.random(), Math.random(), Math.random()));
    TensorUnaryOperator centripedalKnotSpacing = //
        KnotSpacing.centripetal(Se2Parametric.INSTANCE, RealScalar.of(Math.random()));
    Tensor knots = centripedalKnotSpacing.apply(control);
    GeodesicCatmullRom geodesicCatmullRom = GeodesicCatmullRom.of(geodesicSpace, knots, control);
    // ---
    Chop._10.requireClose(geodesicCatmullRom.apply(geodesicCatmullRom.knots().Get(1)), control.get(1));
    Chop._10.requireClose(geodesicCatmullRom.apply(geodesicCatmullRom.knots().Get(2)), control.get(2));
  }

  @Test
  void testLengthFail() {
    Tensor control = RandomVariate.of(UniformDistribution.unit(), 3, 7);
    Tensor knots = KnotSpacing.uniform().apply(control);
    assertThrows(Exception.class, () -> GeodesicCatmullRom.of(RnGroup.INSTANCE, knots, control));
  }

  @Test
  void testKnotsInconsistentFail() {
    Tensor control = RandomVariate.of(UniformDistribution.unit(), 5, 7);
    Tensor knots = KnotSpacing.uniform().apply(control);
    GeodesicCatmullRom.of(RnGroup.INSTANCE, knots, control);
    assertThrows(Exception.class, () -> GeodesicCatmullRom.of(RnGroup.INSTANCE, knots.extract(0, knots.length() - 1), control));
  }
}
