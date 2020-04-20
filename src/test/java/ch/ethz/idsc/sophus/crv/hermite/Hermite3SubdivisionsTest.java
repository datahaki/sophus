// code by jph
package ch.ethz.idsc.sophus.crv.hermite;

import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnTransport;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import junit.framework.TestCase;

public class Hermite3SubdivisionsTest extends TestCase {
  public void testSimple() {
    TestHelper.check(RnHermite3Subdivisions.a1(), Hermite3Subdivisions.a1(RnManifold.HS_EXP, RnTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.check(RnHermite3Subdivisions.a2(), Hermite3Subdivisions.a2(RnManifold.HS_EXP, RnTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.check(RnHermite3Subdivisions.standard(), //
        Hermite3Subdivisions.of(RnManifold.HS_EXP, RnTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
  }

  public void testTension() {
    Scalar theta = RationalScalar.of(2, 157);
    Scalar omega = RationalScalar.of(1, 9);
    HermiteSubdivision hermiteSubdivision = Hermite3Subdivisions.of(RnManifold.HS_EXP, RnTransport.INSTANCE, RnBiinvariantMean.INSTANCE, theta, omega);
    TestHelper.check(RnHermite3Subdivisions.of(theta, omega), hermiteSubdivision);
    TestHelper.checkP(3, hermiteSubdivision);
  }

  public void testH1() {
    Scalar theta = RealScalar.ZERO;
    Scalar omega = RealScalar.ZERO;
    TestHelper.check(Hermite1Subdivisions.standard(RnManifold.HS_EXP, RnTransport.INSTANCE), //
        Hermite3Subdivisions.of(RnManifold.HS_EXP, RnTransport.INSTANCE, RnBiinvariantMean.INSTANCE, theta, omega));
  }

  public void testP1() {
    TestHelper.checkP(1, Hermite3Subdivisions.a1(RnManifold.HS_EXP, RnTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.checkP(1, Hermite3Subdivisions.a2(RnManifold.HS_EXP, RnTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
  }
}
