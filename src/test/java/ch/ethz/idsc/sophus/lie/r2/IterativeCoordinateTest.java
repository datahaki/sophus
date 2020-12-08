// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.util.Random;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.gbc.TargetCoordinate;
import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.hs.s2.S2Exponential;
import ch.ethz.idsc.sophus.hs.s2.S2Manifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.itp.Interpolation;
import ch.ethz.idsc.tensor.itp.LinearInterpolation;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class IterativeCoordinateTest extends TestCase {
  private static void _checkIterative(Genesis genesis) {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = CirclePoints.of(n).add(RandomVariate.of(distribution, n, 2));
      for (int k = 0; k < 5; ++k) {
        Tensor weights = IterativeCoordinate.of(genesis, k).origin(levers);
        Chop._10.requireAllZero(weights.dot(levers));
        {
          Tensor matrix = IterativeCoordinateMatrix.of(k).origin(levers);
          Tensor circum = matrix.dot(levers);
          Tensor wn = NormalizeTotal.FUNCTION.apply(genesis.origin(circum).dot(matrix));
          Chop._10.requireClose(wn, weights);
        }
        MeanDefect meanDefect = new MeanDefect(levers, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        Chop._07.requireAllZero(tangent);
      }
    }
  }

  private static void _checkCornering(Genesis genesis) {
    for (int n = 3; n < 10; ++n) {
      Tensor polygon = CirclePoints.of(n);
      int index = 0;
      for (Tensor x : polygon) {
        Tensor weights = genesis.origin(Tensor.of(polygon.stream().map(x::subtract)));
        Chop._12.requireClose(weights, UnitVector.of(n, index));
        ++index;
      }
    }
  }

  private static void _checkAlongedge(Genesis genesis, boolean strict) {
    Random random = new Random();
    for (int n = 3; n < 10; ++n) {
      Tensor polygon = CirclePoints.of(n);
      int index = random.nextInt(polygon.length() - 1);
      Interpolation interpolation = LinearInterpolation.of(polygon.extract(index, index + 2));
      Tensor x = interpolation.at(RealScalar.of(random.nextDouble()));
      Tensor levers = Tensor.of(polygon.stream().map(x::subtract));
      if (Polygons.isInside(levers) && strict) {
        Tensor weights = genesis.origin(levers);
        Chop._10.requireClose(RnBiinvariantMean.INSTANCE.mean(polygon, weights), x);
      }
    }
  }

  public void testMV() {
    Genesis genesis = ThreePointCoordinate.of(Barycenter.MEAN_VALUE);
    _checkIterative(genesis);
    _checkCornering(genesis);
    _checkAlongedge(genesis, false);
  }

  public void testID() {
    Genesis genesis = MetricCoordinate.of(InversePowerVariogram.of(2));
    _checkIterative(genesis);
    _checkCornering(genesis);
    _checkAlongedge(genesis, true);
  }

  public void testIC() {
    Genesis genesis = IterativeCoordinate.of(MetricCoordinate.affine(), 3);
    _checkCornering(genesis);
    _checkAlongedge(genesis, false);
  }

  public void testKis0() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomVariate.of(distribution, n, 2);
      if (Polygons.isInside(levers)) {
        Tensor weights = ThreePointCoordinate.of(Barycenter.MEAN_VALUE).origin(levers);
        Chop._07.requireClose( //
            weights, //
            IterativeCoordinate.of(ThreePointWeighting.of(Barycenter.MEAN_VALUE), 0).origin(levers));
        if (weights.stream().map(Scalar.class::cast).anyMatch(Sign::isNegative)) {
          boolean result = Chop._10.isClose( //
              weights, //
              IterativeCoordinate.of(ThreePointWeighting.of(Barycenter.MEAN_VALUE), 2).origin(levers));
          if (4 < n)
            assertFalse(result);
        }
      }
    }
  }

  public void testBiinv() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    Genesis genesis = MetricCoordinate.of(InversePowerVariogram.of(2));
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomVariate.of(distribution, n, 2);
      if (Polygons.isInside(levers)) {
        for (int k = 0; k < 3; ++k) {
          Genesis ic = IterativeCoordinate.of(genesis, k);
          Tensor weights = ic.origin(levers);
          MeanDefect meanDefect = new MeanDefect(levers, weights, RnExponential.INSTANCE);
          Tensor tangent = meanDefect.tangent();
          Chop._07.requireAllZero(tangent);
        }
      }
    }
  }

  public void testSimple123() {
    Genesis genesis = MetricCoordinate.affine();
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = CirclePoints.of(n).add(RandomVariate.of(distribution, n, 2));
      for (int k = 0; k < 5; ++k) {
        Tensor weights = IterativeCoordinate.of(genesis, k).origin(levers);
        Chop._10.requireAllZero(weights.dot(levers));
        MeanDefect meanDefect = new MeanDefect(levers, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        Chop._07.requireAllZero(tangent);
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
    int count = 0;
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomSample.of(randomSampleInterface, n);
      HsDesign hsDesign = new HsDesign(S2Manifold.INSTANCE);
      Tensor levers = hsDesign.matrix(sequence, point);
      if (Polygons.isInside(levers)) {
        for (Genesis genesis : GENESIS)
          for (int k = 0; k < 3; ++k) {
            BarycentricCoordinate barycentricCoordinate = //
                HsCoordinates.wrap(S2Manifold.INSTANCE, IterativeCoordinate.of(genesis, k));
            Tensor weights = barycentricCoordinate.weights(sequence, point);
            MeanDefect meanDefect = new MeanDefect(sequence, weights, new S2Exponential(point));
            Tensor tangent = meanDefect.tangent();
            Chop._07.requireAllZero(tangent);
          }
        ++count;
      }
    }
    assertTrue(0 < count);
  }
}
