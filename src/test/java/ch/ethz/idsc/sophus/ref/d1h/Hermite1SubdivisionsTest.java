// code by jph
package ch.ethz.idsc.sophus.ref.d1h;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnTransport;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import junit.framework.TestCase;

public class Hermite1SubdivisionsTest extends TestCase {
  public void testSimple() {
    TestHelper.check( //
        RnHermite1Subdivisions.instance(), //
        Hermite1Subdivisions.standard(RnManifold.HS_EXP, RnTransport.INSTANCE));
  }

  public void testParams() {
    Scalar lambda = RationalScalar.of(-1, 16);
    Scalar mu = RationalScalar.of(-1, 3);
    TestHelper.check( //
        RnHermite1Subdivisions.of(lambda, mu), //
        Hermite1Subdivisions.of(RnManifold.HS_EXP, RnTransport.INSTANCE, lambda, mu));
  }
}
