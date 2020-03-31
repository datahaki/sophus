// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class SinhcInverseTest extends TestCase {
  public void testSimple() {
    assertEquals(SinhcInverse.FUNCTION.apply(RealScalar.ZERO), RealScalar.ONE);
  }

  public void testMin() {
    Scalar eps = DoubleScalar.of(Double.MIN_VALUE);
    Tolerance.CHOP.requireClose(SinhcInverse.FUNCTION.apply(eps), RealScalar.ONE);
  }

  public void testEps() {
    Scalar eps = DoubleScalar.of(1e-12);
    Tolerance.CHOP.requireClose(SinhcInverse.FUNCTION.apply(eps), RealScalar.ONE);
  }
}
