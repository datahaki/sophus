// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.gbc.TargetCoordinate;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.hs.s2.S2Exponential;
import ch.ethz.idsc.sophus.hs.s2.S2Manifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class IterativeCoordinateTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 2);
      {
        Genesis tensorUnaryOperator = ThreePointWeighting.of(Barycenter.MEAN_VALUE);
        Tensor weights = tensorUnaryOperator.origin(sequence);
        MeanDefect meanDefect = new MeanDefect(sequence, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        Chop._07.requireAllZero(tangent);
      }
      for (int k = 0; k < 3; ++k) {
        Genesis tensorUnaryOperator = IterativeCoordinate.meanValue(k);
        Tensor weights = tensorUnaryOperator.origin(sequence);
        MeanDefect meanDefect = new MeanDefect(sequence, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        Chop._07.requireAllZero(tangent);
      }
    }
  }

  public void testKis0() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    // for (int count = 0; count < 10; ++count)
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 2);
      Tensor weights = ThreePointCoordinate.of(Barycenter.MEAN_VALUE).origin(sequence);
      Chop._07.requireClose( //
          weights, //
          new IterativeCoordinate(ThreePointWeighting.of(Barycenter.MEAN_VALUE), 0).origin(sequence));
      if (weights.stream().map(Scalar.class::cast).anyMatch(Sign::isNegative)) {
        boolean result = Chop._10.isClose( //
            weights, //
            new IterativeCoordinate(ThreePointWeighting.of(Barycenter.MEAN_VALUE), 2).origin(sequence));
        if (4 < n)
          assertFalse(result);
      }
    }
  }

  public void testBiinv() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    Genesis ops = MetricCoordinate.of(InversePowerVariogram.of(2));
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 2);
      for (int k = 0; k < 3; ++k) {
        Genesis tensorUnaryOperator = IterativeCoordinate.of(ops, k);
        // TensorUnaryOperator ivd = points->
        // Biinvariants.METRIC.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(-2), points);
        Tensor weights = tensorUnaryOperator.origin(sequence);
        MeanDefect meanDefect = new MeanDefect(sequence, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        if (!Chop._07.allZero(tangent)) {
          System.out.println("ICTEST n=" + n + " k=" + k);
        }
      }
    }
  }

  private static final Genesis[] GENESIS = { //
      ThreePointCoordinate.of(Barycenter.MEAN_VALUE), //
      MetricCoordinate.affine(), //
      TargetCoordinate.of(InversePowerVariogram.of(2)), //
  };

  public void testS2() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    Tensor point = UnitVector.of(3, 0);
    int fails = 0;
    for (Genesis genesis : GENESIS)
      for (int n = 3; n < 10; ++n)
        for (int k = 0; k < 3; ++k)
          try {
            Tensor sequence = RandomSample.of(randomSampleInterface, n);
            BarycentricCoordinate barycentricCoordinate = //
                HsCoordinates.wrap(S2Manifold.INSTANCE, IterativeCoordinate.of(genesis, k));
            Tensor weights = barycentricCoordinate.weights(sequence, point);
            MeanDefect meanDefect = new MeanDefect(sequence, weights, new S2Exponential(point));
            Tensor tangent = meanDefect.tangent();
            if (!Chop._07.allZero(tangent)) {
              System.out.println("S2TEST n=" + n + " k=" + k);
              ++fails;
            }
          } catch (Exception e) {
            ++fails;
          }
    assertTrue(fails < 4);
  }
}
