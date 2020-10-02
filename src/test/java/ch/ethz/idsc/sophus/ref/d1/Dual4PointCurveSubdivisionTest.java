// code by jph
package ch.ethz.idsc.sophus.ref.d1;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import junit.framework.TestCase;

public class Dual4PointCurveSubdivisionTest extends TestCase {
  public void testSimple() {
    AssertFail.of(() -> new Dual4PointCurveSubdivision(null, RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO));
  }
}
