// code by jph
package ch.alpine.sophus.ref.d1h;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

class Hermite3SubdivisionsTest {
  @Test
  public void testStandardCompare() {
    TestHelper.check(RnHermite3Subdivisions.standard(), //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.check(RnHermite3Subdivisions.standard(), //
        Hermite3Subdivisions.of(RnManifold.INSTANCE));
  }

  @Test
  public void testA1Compare() {
    TestHelper.check(RnHermite3Subdivisions.a1(), Hermite3Subdivisions.a1(RnManifold.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.check(RnHermite3Subdivisions.a1(), Hermite3Subdivisions.a1(RnManifold.INSTANCE));
  }

  @Test
  public void testA2Compare() {
    TestHelper.check(RnHermite3Subdivisions.a2(), Hermite3Subdivisions.a2(RnManifold.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.check(RnHermite3Subdivisions.a2(), Hermite3Subdivisions.a2(RnManifold.INSTANCE));
  }

  @Test
  public void testTension() {
    Scalar theta = RationalScalar.of(2, 157);
    Scalar omega = RationalScalar.of(1, 9);
    HermiteSubdivision hermiteSubdivision = Hermite3Subdivisions.of(RnManifold.INSTANCE, RnBiinvariantMean.INSTANCE, theta, omega);
    TestHelper.check(RnHermite3Subdivisions.of(theta, omega), hermiteSubdivision);
    TestHelper.checkP(3, hermiteSubdivision);
  }

  @Test
  public void testH1() {
    Scalar theta = RealScalar.ZERO;
    Scalar omega = RealScalar.ZERO;
    TestHelper.check(Hermite1Subdivisions.standard(RnManifold.INSTANCE), //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, RnBiinvariantMean.INSTANCE, theta, omega));
    TestHelper.check(Hermite1Subdivisions.standard(RnManifold.INSTANCE), //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, theta, omega));
  }

  @Test
  public void testP1() {
    TestHelper.checkP(1, Hermite3Subdivisions.a1(RnManifold.INSTANCE, RnBiinvariantMean.INSTANCE));
    TestHelper.checkP(1, Hermite3Subdivisions.a2(RnManifold.INSTANCE, RnBiinvariantMean.INSTANCE));
  }
}
