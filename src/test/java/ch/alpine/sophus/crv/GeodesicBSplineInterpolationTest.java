// code by jph
package ch.alpine.sophus.crv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.AbstractBSplineInterpolation.Iteration;
import ch.alpine.sophus.hs.h2.H2Geodesic;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;

class GeodesicBSplineInterpolationTest {
  /* package */ static Tensor pet(Tensor prev, Tensor eval, Tensor goal) {
    return prev.add(goal.subtract(eval));
  }

  @Test
  public void testApplyRn() {
    Tensor target = N.DOUBLE.of(Tensors.vector(1, 2, 0, 2, 1, 3));
    AbstractBSplineInterpolation geodesicBSplineInterpolation = //
        new GeodesicBSplineInterpolation(RnGeodesic.INSTANCE, 2, target);
    Tensor control = geodesicBSplineInterpolation.apply();
    Tensor vector = Tensors.vector(1, 2.7510513036161504, -0.922624053826282, 2.784693019343523, 0.21446593776315992, 3);
    Chop._10.requireClose(control, vector);
  }

  @Test
  public void testMoveRn() {
    Tensor tensor = RandomVariate.of(DiscreteUniformDistribution.of(2, 100), 3, 5);
    AbstractBSplineInterpolation geodesicBSplineInterpolation = //
        new GeodesicBSplineInterpolation(RnGeodesic.INSTANCE, 2, tensor);
    Tensor prev = tensor.get(0);
    Tensor eval = tensor.get(1);
    Tensor goal = tensor.get(2);
    Tensor pos0 = geodesicBSplineInterpolation.move(prev, eval, goal);
    Tensor pos1 = pet(prev, eval, goal);
    assertEquals(pos0, pos1);
    ExactTensorQ.require(pos0);
    ExactTensorQ.require(pos1);
    Iteration iteration = geodesicBSplineInterpolation.init();
    assertEquals(iteration.steps(), 0);
  }

  @Test
  public void testH2a() {
    Tensor target = Tensors.fromString("{{0, 2}, {1, 2}, {2, 2}}");
    AbstractBSplineInterpolation geodesicBSplineInterpolation = //
        new GeodesicBSplineInterpolation(H2Geodesic.INSTANCE, 3, target);
    target = N.DOUBLE.of(target);
    Iteration iteration = geodesicBSplineInterpolation.untilClose(Chop._08, 100);
    assertTrue(iteration.steps() < 200);
  }

  @Test
  public void testH2b() {
    Tensor target = Tensors.fromString("{{0, 1}, {1, 1}, {2, 1}}");
    AbstractBSplineInterpolation geodesicBSplineInterpolation = //
        new GeodesicBSplineInterpolation(H2Geodesic.INSTANCE, 3, target);
    target = N.DOUBLE.of(target);
    Iteration iteration = geodesicBSplineInterpolation.untilClose(Chop._08, 200);
    assertTrue(iteration.steps() < 250);
  }

  @Test
  public void testH2c() {
    Tensor target = Tensors.fromString("{{0, 1}, {1, 1}, {2, 0.1}, {3, 1}}");
    AbstractBSplineInterpolation geodesicBSplineInterpolation = //
        new GeodesicBSplineInterpolation(H2Geodesic.INSTANCE, 3, target);
    target = N.DOUBLE.of(target);
    Iteration iteration = geodesicBSplineInterpolation.untilClose(Chop._08, 200);
    assertTrue(iteration.steps() < 150);
  }

  @Test
  public void testH2d() {
    Tensor target = Tensors.fromString("{{2/5, 3/5}, {32/15, 77/60}, {15/4, 11/15}, {73/15, 71/30}, {13/2, 1/2}}");
    AbstractBSplineInterpolation geodesicBSplineInterpolation = //
        new GeodesicBSplineInterpolation(H2Geodesic.INSTANCE, 3, target);
    target = N.DOUBLE.of(target);
    Iteration iteration = geodesicBSplineInterpolation.untilClose(Chop._08, 100);
    assertTrue(iteration.steps() < 100);
  }
}
