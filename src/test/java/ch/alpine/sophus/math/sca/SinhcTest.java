// code by jph
package ch.alpine.sophus.math.sca;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.qty.Quantity;
import junit.framework.TestCase;

public class SinhcTest extends TestCase {
  public void testSimple() {
    assertEquals(Sinhc.FUNCTION.apply(RealScalar.ZERO), RealScalar.ONE);
  }

  public void testMin() {
    Scalar eps = DoubleScalar.of(Double.MIN_VALUE);
    Tolerance.CHOP.requireClose(Sinhc.FUNCTION.apply(eps), RealScalar.ONE);
  }

  public void testEps() {
    Scalar eps = DoubleScalar.of(1e-12);
    Tolerance.CHOP.requireClose(Sinhc.FUNCTION.apply(eps), RealScalar.ONE);
  }

  public void testFail() {
    AssertFail.of(() -> Sinhc.FUNCTION.apply(Quantity.of(0, "m")));
  }
}
