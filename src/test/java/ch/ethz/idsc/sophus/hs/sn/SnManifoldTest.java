// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.util.Random;

import ch.ethz.idsc.sophus.bm.MeanDefect;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.lie.so.SoRandomSample;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnManifoldTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = //
      GbcHelper.barycentrics(SnManifold.INSTANCE);

  private static Tensor randomCloud(Tensor mean, int n) {
    Distribution distribution = NormalDistribution.of(0, 0.1);
    return Tensor.of(RandomVariate.of(distribution, n, mean.length()).stream().map(mean::add).map(Vector2Norm.NORMALIZE));
  }

  public void testLinearReproduction() {
    Random random = new Random();
    int fails = 0;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 5; ++d) {
        Tensor mean = UnitVector.of(d, 0);
        int n = d + 1 + random.nextInt(3);
        try {
          Tensor sequence = randomCloud(mean, n);
          Tensor weights = barycentricCoordinate.weights(sequence, mean);
          VectorQ.requireLength(weights, n);
          AffineQ.require(weights, Chop._08);
          Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
          Chop._06.requireAllZero(evaluate);
          Chop._06.requireClose(mean, SnBiinvariantMean.INSTANCE.mean(sequence, weights));
        } catch (Exception exception) {
          exception.printStackTrace();
          ++fails;
        }
      }
    assertTrue(fails <= 2);
  }

  public void testLagrangeProperty() {
    Random random = new Random();
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 4; ++d) {
        int n = d + 1 + random.nextInt(3);
        Tensor sequence = randomCloud(UnitVector.of(d, 0), n);
        int count = random.nextInt(sequence.length());
        Tensor mean = sequence.get(count);
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        VectorQ.requireLength(weights, n);
        AffineQ.require(weights, Chop._08);
        Chop._06.requireClose(weights, UnitVector.of(n, count));
        Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
        Chop._06.requireAllZero(evaluate);
        Chop._03.requireClose(mean, SnBiinvariantMean.of(Chop._06).mean(sequence, weights));
      }
  }

  public void testBiinvariance() {
    Random random = new Random();
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 5; ++d) {
        Tensor mean = UnitVector.of(d, 0);
        RandomSampleInterface randomSampleInterface = SoRandomSample.of(d);
        int n = d + 1 + random.nextInt(3);
        Tensor sequence = randomCloud(mean, n);
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        VectorQ.requireLength(weights, n);
        AffineQ.require(weights, Chop._08);
        {
          Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
          Chop._08.requireAllZero(evaluate);
        }
        // ---
        {
          Tensor matrix = RandomSample.of(randomSampleInterface);
          Tensor mean2 = matrix.dot(mean);
          Tensor shifted = Tensor.of(sequence.stream().map(matrix::dot));
          Tensor evaluate = new MeanDefect(shifted, weights, SnManifold.INSTANCE.exponential(mean2)).tangent();
          Chop._10.requireAllZero(evaluate);
          Tensor weights2 = barycentricCoordinate.weights(shifted, mean2);
          Chop._06.requireClose(weights, weights2);
        }
      }
  }
}
