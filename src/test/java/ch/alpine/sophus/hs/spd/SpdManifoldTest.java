// code by jph
package ch.alpine.sophus.hs.spd;

import java.util.Random;

import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.gbc.LeveragesCoordinate;
import ch.alpine.sophus.gbc.MetricCoordinate;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdManifoldTest extends TestCase {
  public static final BarycentricCoordinate[] list() {
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
    Random random = new Random();
    int d = 2;
    int fail = 0;
    int len = 5 + random.nextInt(3);
    Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
    for (BarycentricCoordinate barycentricCoordinate : list())
      try {
        Tensor point = TestHelper.generateSpd(d);
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        AffineQ.require(weights, Chop._08);
        Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
        Chop._08.requireClose(spd, point);
      } catch (Exception exception) {
        ++fail;
      }
    assertTrue(fail < 4);
  }

  public void testLagrangeProperty() {
    Random random = new Random();
    int d = 2;
    int len = 5 + random.nextInt(3);
    Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(d), len);
    for (BarycentricCoordinate barycentricCoordinate : list()) {
      int index = random.nextInt(sequence.length());
      Tensor point = sequence.get(index);
      Tensor weights = barycentricCoordinate.weights(sequence, point);
      AffineQ.require(weights, Chop._08);
      Chop._06.requireClose(weights, UnitVector.of(len, index));
      Tensor spd = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
      Chop._08.requireClose(spd, point);
    }
  }

  public void testFlipMidpoint() {
    Tensor p = TestHelper.generateSpd(3);
    Tensor q = TestHelper.generateSpd(3);
    Tolerance.CHOP.requireClose(new SpdExponential(p).flip(q), SpdManifold.INSTANCE.flip(p, q));
    Tolerance.CHOP.requireClose(new SpdExponential(p).midpoint(q), SpdManifold.INSTANCE.midpoint(p, q));
  }
}
