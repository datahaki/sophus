// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class ClothoidApproximationTest extends TestCase {
  public void testSimple() {
    Scalar f = ClothoidApproximation.f(RealScalar.of(0.3), RealScalar.of(-0.82));
    Tolerance.CHOP.requireClose(f, RealScalar.of(0.1213890127877238));
  }
}
