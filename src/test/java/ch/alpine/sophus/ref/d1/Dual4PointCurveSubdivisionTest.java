// code by jph
package ch.alpine.sophus.ref.d1;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;

public class Dual4PointCurveSubdivisionTest {
  @Test
  public void testSimple() {
    AssertFail.of(() -> new Dual4PointCurveSubdivision(null, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO));
  }
}
