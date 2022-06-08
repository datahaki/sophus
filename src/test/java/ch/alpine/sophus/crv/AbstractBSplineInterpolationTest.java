// code by jph
package ch.alpine.sophus.crv;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.AbstractBSplineInterpolation.Iteration;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.sca.Chop;

class AbstractBSplineInterpolationTest {
  @Test
  public void testS2() {
    Tensor target = Tensors.fromString("{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}, {-1, 0, 0}}");
    AbstractBSplineInterpolation geodesicBSplineInterpolation = //
        new GeodesicBSplineInterpolation(SnManifold.INSTANCE, 2, target);
    Iteration iteration = geodesicBSplineInterpolation.untilClose(Chop._08, 100);
    assertTrue(iteration.steps() < 100);
    Tensor control = iteration.control();
    Chop._12.requireClose(control.get(0), target.get(0));
    Chop._12.requireClose(control.get(3), target.get(3));
    GeodesicBSplineFunction geodesicBSplineFunction = //
        GeodesicBSplineFunction.of(SnManifold.INSTANCE, 2, control);
    Chop._06.requireClose(target, Range.of(0, 4).map(geodesicBSplineFunction));
  }

  @Test
  public void testS2Jacobi() {
    Tensor target = Tensors.fromString("{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}, {-1, 0, 0}}");
    AbstractBSplineInterpolation geodesicBSplineInterpolation = //
        new GeodesicBSplineInterpolation(SnManifold.INSTANCE, 2, target);
    Iteration iteration = geodesicBSplineInterpolation.init();
    assertTrue(iteration.steps() < 100);
    for (int c = 0; c < 20; ++c)
      iteration = iteration.stepJacobi();
    Tensor control = iteration.control();
    Chop._12.requireClose(control.get(0), target.get(0));
    Chop._12.requireClose(control.get(3), target.get(3));
    GeodesicBSplineFunction geodesicBSplineFunction = //
        GeodesicBSplineFunction.of(SnManifold.INSTANCE, 2, control);
    Chop._06.requireClose(target, Range.of(0, 4).map(geodesicBSplineFunction));
  }
}
