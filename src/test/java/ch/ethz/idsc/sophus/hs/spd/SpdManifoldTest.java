// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.util.Random;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.gbc.LeveragesCoordinate;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdManifoldTest extends TestCase {
  public static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES(Tensor sequence) {
    // return GbcHelper.barycentrics(SpdManifold.INSTANCE);
    return new BarycentricCoordinate[] { //
        HsCoordinates.wrap(SpdManifold.INSTANCE, MetricCoordinate.of(InversePowerVariogram.of(1))), //
        HsCoordinates.wrap(SpdManifold.INSTANCE, MetricCoordinate.of(InversePowerVariogram.of(2))), //
        // LeveragesCoordinate.slow(SpdManifold.INSTANCE, InversePowerVariogram.of(1)), //
        // LeveragesCoordinate.slow(SpdManifold.INSTANCE, InversePowerVariogram.of(2)), //
        LeveragesCoordinate.of(SpdManifold.INSTANCE, InversePowerVariogram.of(1)), //
        LeveragesCoordinate.of(SpdManifold.INSTANCE, InversePowerVariogram.of(2)), //
    };
  }

  public void testSimple() {
    int d = 2;
    int fail = 0;
    for (int len = 5; len < 10; ++len)
      try {
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
        for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES(sequence)) {
          Tensor point = TestHelper.generateSpd(d);
          Tensor weights = barycentricCoordinate.weights(sequence, point);
          AffineQ.require(weights, Chop._08);
          Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._08.requireClose(spd, point);
        }
      } catch (Exception exception) {
        ++fail;
      }
    assertTrue(fail < 3);
  }
  // public void test3x3() {
  // int d = 3;
  // int errors = 0;
  // for (int len = 7; len < 11; ++len) {
  // Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
  // for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES(sequence)) {
  // Tensor point = TestHelper.generateSpd(d);
  // Tensor weights = barycentricCoordinate.weights(sequence, point);
  // AffineQ.require(weights);
  // try {
  // Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
  // Chop._06.requireClose(spd, point);
  // } catch (Exception exception) {
  // ++errors;
  // }
  // }
  // }
  // assertTrue(errors < 5);
  // }

  public void testLagrangeProperty() {
    Random random = new Random();
    int d = 2;
    for (int len = 5; len < 8; ++len) {
      Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
      for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES(sequence)) {
        int index = random.nextInt(sequence.length());
        Tensor point = sequence.get(index);
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        AffineQ.require(weights, Chop._08);
        Chop._06.requireClose(weights, UnitVector.of(len, index));
        Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
        Chop._08.requireClose(spd, point);
      }
    }
  }
}
