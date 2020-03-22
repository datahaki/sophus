// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LagrangeQuadraticDTest extends TestCase {
  public void testSimple() {
    LagrangeQuadraticD lagrangeQuadraticD = new LagrangeQuadraticD(RealScalar.of(0.7), RealScalar.of(0.3), RealScalar.of(-0.82));
    Scalar f = lagrangeQuadraticD.apply(RealScalar.of(2));
    Chop._15.requireClose(f, RealScalar.of(-5.84));
  }
}
