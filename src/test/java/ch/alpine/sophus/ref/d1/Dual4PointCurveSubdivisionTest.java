// code by jph
package ch.alpine.sophus.ref.d1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;

class Dual4PointCurveSubdivisionTest {
  @Test
  public void testSimple() {
    assertThrows(Exception.class, () -> new Dual4PointCurveSubdivision(null, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO));
  }
}
