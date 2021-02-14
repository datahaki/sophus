// code by jph
package ch.ethz.idsc.sophus.ref.d1h;

import ch.ethz.idsc.sophus.lie.LieTransport;
import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import junit.framework.TestCase;

public class Hermite3SubdivisionsTest extends TestCase {
  public void testStandardCompare() {
    TestHelper.check(RnHermite3Subdivisions.standard(), //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.check(RnHermite3Subdivisions.standard(), //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE));
  }

  public void testA1Compare() {
    TestHelper.check(RnHermite3Subdivisions.a1(), Hermite3Subdivisions.a1(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.check(RnHermite3Subdivisions.a1(), Hermite3Subdivisions.a1(RnManifold.INSTANCE, LieTransport.INSTANCE));
  }

  public void testA2Compare() {
    TestHelper.check(RnHermite3Subdivisions.a2(), Hermite3Subdivisions.a2(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.check(RnHermite3Subdivisions.a2(), Hermite3Subdivisions.a2(RnManifold.INSTANCE, LieTransport.INSTANCE));
  }

  public void testTension() {
    Scalar theta = RationalScalar.of(2, 157);
    Scalar omega = RationalScalar.of(1, 9);
    HermiteSubdivision hermiteSubdivision = Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE, theta, omega);
    TestHelper.check(RnHermite3Subdivisions.of(theta, omega), hermiteSubdivision);
    TestHelper.checkP(3, hermiteSubdivision);
  }

  public void testH1() {
    Scalar theta = RealScalar.ZERO;
    Scalar omega = RealScalar.ZERO;
    TestHelper.check(Hermite1Subdivisions.standard(RnManifold.INSTANCE, LieTransport.INSTANCE), //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE, theta, omega));
    TestHelper.check(Hermite1Subdivisions.standard(RnManifold.INSTANCE, LieTransport.INSTANCE), //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, theta, omega));
  }

  public void testP1() {
    TestHelper.checkP(1, Hermite3Subdivisions.a1(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.checkP(1, Hermite3Subdivisions.a2(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
  }
}
