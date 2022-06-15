// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class Se3ManifoldTest {
  private static final IterativeBiinvariantMean ITERATIVE_BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.argmax(Se3Group.INSTANCE, Chop._12);
  private static final BarycentricCoordinate[] ALL_COORDINATES = GbcHelper.barycentrics(Se3Group.INSTANCE);
  private static final BarycentricCoordinate[] BII_COORDINATES = //
      GbcHelper.biinvariant(Se3Group.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se3Group.INSTANCE);
  private static final RandomSampleInterface RSI_Se3 = new Se3RandomSample( //
      UniformDistribution.of(Clips.absolute(5)), //
      TriangularDistribution.with(0, 0.25));

  @Test
  void testRandom() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES) {
      int n = 7 + random.nextInt(4);
      Tensor sequence = RandomSample.of(RSI_Se3, random, n);
      Tensor point = RandomSample.of(RSI_Se3, random);
      {
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        AffineQ.require(weights, Chop._08);
      }
      {
        Tensor weights = RandomVariate.of(TriangularDistribution.with(1, 0.3), n);
        weights = NormalizeTotal.FUNCTION.apply(weights);
        Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
        assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
        Tensor defect = new MeanDefect(sequence, weights, Se3Group.INSTANCE.exponential(mean)).tangent();
        Chop._08.requireAllZero(defect);
      }
    }
  }

  @Test
  void testMeanRandom() {
    Distribution distribution = NormalDistribution.of(4, 1);
    for (int n = 7; n < 13; ++n) {
      Tensor sequence = RandomSample.of(RSI_Se3, n);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
      Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
      assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
      Tensor defect = new MeanDefect(sequence, weights, Se3Group.INSTANCE.exponential(mean)).tangent();
      assertEquals(Dimensions.of(defect), Arrays.asList(6));
      Chop._08.requireAllZero(defect);
    }
  }

  @Test
  void testRelativeRandom() {
    Random random = new Random(3);
    BiinvariantMean biinvariantMean = ITERATIVE_BIINVARIANT_MEAN;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
      int n = 7 + random.nextInt(3);
      Tensor points = RandomSample.of(RSI_Se3, random, n);
      Tensor xya = RandomSample.of(RSI_Se3, random);
      Tensor weights = barycentricCoordinate.weights(points, xya);
      AffineQ.require(weights, Chop._08);
      Tensor check1 = biinvariantMean.mean(points, weights);
      Chop._10.requireClose(check1, xya);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(xya, x_recreated);
      Tensor shift = RandomSample.of(RSI_Se3, random);
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor all = tensorMapping.slash(points);
        Tensor one = tensorMapping.apply(xya);
        Chop._10.requireClose(one, biinvariantMean.mean(all, weights));
        Chop._05.requireClose(weights, barycentricCoordinate.weights(all, one));
      }
    }
  }
}
