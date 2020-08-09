// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.util.Random;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.son.SonRandomSample;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnManifoldTest extends TestCase {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = GbcHelper.barycentrics(SnManifold.INSTANCE);
  private static final MeanDefect MEAN_DEFECT = new MeanDefect(SnManifold.INSTANCE);

  public void testLinearReproduction() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    int fails = 0;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 3; d < 7; ++d)
        for (int n = d + 1; n < d + 3; ++n)
          try {
            Tensor mean = UnitVector.of(d, 0);
            Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream().map(mean::add).map(NORMALIZE));
            Tensor weights = barycentricCoordinate.weights(sequence, mean);
            VectorQ.requireLength(weights, n);
            AffineQ.require(weights);
            Tensor evaluate = MEAN_DEFECT.defect(sequence, weights, mean);
            Chop._06.requireAllZero(evaluate);
            Chop._06.requireClose(mean, SnBiinvariantMean.INSTANCE.mean(sequence, weights));
          } catch (Exception exception) {
            ++fails;
          }
    assertTrue(fails < 4);
  }

  public void testLagrangeProperty() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    int fails = 0;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 3; d < 7; ++d)
        for (int n = d + 1; n < d + 3; ++n)
          try {
            Tensor center = UnitVector.of(d, 0);
            Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream().map(center::add).map(NORMALIZE));
            int count = 0;
            for (Tensor mean : sequence) {
              Tensor weights = barycentricCoordinate.weights(sequence, mean);
              VectorQ.requireLength(weights, n);
              AffineQ.require(weights);
              Chop._06.requireClose(weights, UnitVector.of(n, count));
              Tensor evaluate = MEAN_DEFECT.defect(sequence, weights, mean);
              Chop._06.requireAllZero(evaluate);
              Chop._03.requireClose(mean, SnBiinvariantMean.of(Chop._06).mean(sequence, weights));
              ++count;
            }
          } catch (Exception exception) {
            ++fails;
          }
    assertTrue(fails < 3);
  }

  public void testBiinvariance() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    Random random = new Random();
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 3; d < 7; ++d) {
        RandomSampleInterface randomSampleInterface = SonRandomSample.of(d);
        for (int n = d + 1; n < d + 3; ++n) {
          Tensor mean = UnitVector.of(d, 0);
          Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream().map(mean::add).map(NORMALIZE));
          Tensor weights = barycentricCoordinate.weights(sequence, mean);
          VectorQ.requireLength(weights, n);
          AffineQ.require(weights);
          {
            Tensor evaluate = MEAN_DEFECT.defect(sequence, weights, mean);
            Chop._12.requireAllZero(evaluate);
          }
          // ---
          {
            Tensor matrix = randomSampleInterface.randomSample(random);
            Tensor evaluate = MEAN_DEFECT.defect(Tensor.of(sequence.stream().map(matrix::dot)), weights, matrix.dot(mean));
            Chop._12.requireAllZero(evaluate);
          }
        }
      }
  }
}
