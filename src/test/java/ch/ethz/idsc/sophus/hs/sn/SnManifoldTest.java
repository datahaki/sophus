// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.son.SonRandomSample;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnManifoldTest extends TestCase {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = //
      GbcHelper.barycentrics(SnManifold.INSTANCE);

  private static Tensor randomCloud(Tensor mean, int n) {
    Distribution distribution = NormalDistribution.of(0, 0.1);
    return Tensor.of(RandomVariate.of(distribution, n, mean.length()).stream().map(mean::add).map(NORMALIZE));
  }

  public void testLinearReproduction() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 6; ++d) {
        Tensor mean = UnitVector.of(d, 0);
        for (int n = d + 1; n < d + 3; ++n) {
          Tensor sequence = randomCloud(mean, n);
          Tensor weights = barycentricCoordinate.weights(sequence, mean);
          VectorQ.requireLength(weights, n);
          AffineQ.require(weights, Chop._08);
          Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
          Chop._06.requireAllZero(evaluate);
          Chop._06.requireClose(mean, SnBiinvariantMean.INSTANCE.mean(sequence, weights));
        }
      }
  }

  public void testLagrangeProperty() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 6; ++d) {
        for (int n = d + 1; n < d + 3; ++n) {
          Tensor sequence = randomCloud(UnitVector.of(d, 0), n);
          int count = 0;
          for (Tensor mean : sequence) {
            Tensor weights = barycentricCoordinate.weights(sequence, mean);
            VectorQ.requireLength(weights, n);
            AffineQ.require(weights, Chop._08);
            Chop._06.requireClose(weights, UnitVector.of(n, count));
            Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
            Chop._06.requireAllZero(evaluate);
            Chop._03.requireClose(mean, SnBiinvariantMean.of(Chop._06).mean(sequence, weights));
            ++count;
          }
        }
      }
  }

  public void testBiinvariance() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 6; ++d) {
        Tensor mean = UnitVector.of(d, 0);
        RandomSampleInterface randomSampleInterface = SonRandomSample.of(d);
        for (int n = d + 1; n < d + 3; ++n) {
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
}
