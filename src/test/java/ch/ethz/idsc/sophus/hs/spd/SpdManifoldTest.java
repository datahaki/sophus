// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.gbc.AbsoluteCoordinate;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.HsInverseDistanceCoordinate;
import ch.ethz.idsc.sophus.gbc.RelativeCoordinate;
import ch.ethz.idsc.sophus.krg.ShepardWeighting;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdManifoldTest extends TestCase {
  public static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      RelativeCoordinate.linear(SpdManifold.INSTANCE), //
      RelativeCoordinate.smooth(SpdManifold.INSTANCE), //
      AbsoluteCoordinate.linear(SpdManifold.INSTANCE), //
      AbsoluteCoordinate.smooth(SpdManifold.INSTANCE), //
      HsInverseDistanceCoordinate.custom(SpdManifold.INSTANCE, ShepardWeighting.absolute(SpdManifold.INSTANCE, 1)), //
      HsInverseDistanceCoordinate.custom(SpdManifold.INSTANCE, ShepardWeighting.absolute(SpdManifold.INSTANCE, 2)) };

  public void testSimple() {
    int d = 2;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      int fail = 0;
      for (int len = 5; len < 10; ++len)
        try {
          Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
          Tensor point = TestHelper.generateSpd(d);
          Tensor weights = barycentricCoordinate.weights(sequence, point);
          AffineQ.require(weights);
          Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._08.requireClose(spd, point);
        } catch (Exception exception) {
          ++fail;
        }
      assertTrue(fail < 3);
    }
  }

  public void test3x3() {
    int d = 3;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      int errors = 0;
      for (int len = 7; len < 16; ++len) {
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
        Tensor point = TestHelper.generateSpd(d);
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        AffineQ.require(weights);
        try {
          Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._08.requireClose(spd, point);
        } catch (Exception exception) {
          ++errors;
        }
      }
      assertTrue(errors < 4);
    }
  }

  public void testLagrangeProperty() {
    int d = 2;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int len = 5; len < 10; ++len) {
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
        int index = 0;
        for (Tensor point : sequence) {
          Tensor weights = barycentricCoordinate.weights(sequence, point);
          AffineQ.require(weights);
          Tolerance.CHOP.requireClose(weights, UnitVector.of(len, index));
          Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._08.requireClose(spd, point);
          ++index;
        }
      }
  }
}
