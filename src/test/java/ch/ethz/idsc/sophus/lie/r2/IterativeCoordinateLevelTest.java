// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.opt.TensorScalarFunction;
import junit.framework.TestCase;

public class IterativeCoordinateLevelTest extends TestCase {
  public void testSimple() {
    TensorScalarFunction tensorScalarFunction = IterativeCoordinateLevel.usingMeanValue();
    Scalar apply = tensorScalarFunction.apply(CirclePoints.of(5));
    System.out.println(apply);
  }
}
