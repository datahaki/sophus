package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.RealScalar;
import junit.framework.TestCase;

public class IdentRampTest extends TestCase {
  public void testSimple() {
    assertEquals(IdentRamp.FUNCTION.apply(RealScalar.of(-0.5)), RealScalar.of(+0.5));
    assertEquals(IdentRamp.FUNCTION.apply(RealScalar.of(+0.5)), RealScalar.of(+0.0));
    assertEquals(IdentRamp.FUNCTION.apply(RealScalar.of(+1.5)), RealScalar.of(-0.5));
  }
}
