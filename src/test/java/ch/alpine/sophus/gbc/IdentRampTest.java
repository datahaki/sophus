// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.gbc.amp.IdentRamp;
import ch.alpine.tensor.RealScalar;
import junit.framework.TestCase;

public class IdentRampTest extends TestCase {
  public void testSimple() {
    assertEquals(IdentRamp.FUNCTION.apply(RealScalar.of(-0.5)), RealScalar.of(+0.5));
    assertEquals(IdentRamp.FUNCTION.apply(RealScalar.of(+0.5)), RealScalar.of(+0.0));
    assertEquals(IdentRamp.FUNCTION.apply(RealScalar.of(+1.5)), RealScalar.of(-0.5));
  }
}
