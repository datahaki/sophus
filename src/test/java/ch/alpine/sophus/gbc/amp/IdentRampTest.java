// code by jph
package ch.alpine.sophus.gbc.amp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;

class IdentRampTest {
  @Test
  void testSimple() {
    assertEquals(IdentRamp.FUNCTION.apply(RealScalar.of(-0.5)), RealScalar.of(+0.5));
    assertEquals(IdentRamp.FUNCTION.apply(RealScalar.of(+0.5)), RealScalar.of(+0.0));
    assertEquals(IdentRamp.FUNCTION.apply(RealScalar.of(+1.5)), RealScalar.of(-0.5));
  }
}
