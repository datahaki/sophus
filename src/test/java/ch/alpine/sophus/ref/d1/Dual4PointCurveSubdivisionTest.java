// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import junit.framework.TestCase;

public class Dual4PointCurveSubdivisionTest extends TestCase {
  public void testSimple() {
    AssertFail.of(() -> new Dual4PointCurveSubdivision(null, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO));
  }
}
