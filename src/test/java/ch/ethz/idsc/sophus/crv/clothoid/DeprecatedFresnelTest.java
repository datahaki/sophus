// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class DeprecatedFresnelTest extends TestCase {
  public void testCSimple() {
    Scalar scalar = DeprecatedFresnel.C().apply(RealScalar.ZERO);
    assertEquals(scalar, RealScalar.ZERO);
  }

  public void testCOneP() {
    Scalar scalar = DeprecatedFresnel.C().apply(RealScalar.ONE);
    Chop._12.requireClose(scalar, RealScalar.of(+0.904524237900272));
  }

  public void testCOneN() {
    Scalar scalar = DeprecatedFresnel.C().apply(RealScalar.ONE.negate());
    Chop._12.requireClose(scalar, RealScalar.of(-0.904524237900272));
  }

  public void testSSimple() {
    Scalar scalar = DeprecatedFresnel.S().apply(RealScalar.ZERO);
    assertEquals(scalar, RealScalar.ZERO);
  }

  public void testSOneP() {
    Scalar scalar = DeprecatedFresnel.S().apply(RealScalar.ONE);
    Chop._12.requireClose(scalar, RealScalar.of(+0.3102683017233811));
  }

  public void testSOneN() {
    Scalar scalar = DeprecatedFresnel.S().apply(RealScalar.ONE.negate());
    Chop._12.requireClose(scalar, RealScalar.of(-0.3102683017233811));
  }
}