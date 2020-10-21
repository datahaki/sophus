// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class IterativeCoordinateLevelTest extends TestCase {
  public void testSimple() {
    TensorScalarFunction tensorScalarFunction = IterativeCoordinateLevel.usingMeanValue(Tolerance.CHOP, 3);
    Scalar apply = tensorScalarFunction.apply(CirclePoints.of(5));
    assertEquals(apply, RealScalar.ZERO);
  }
}
