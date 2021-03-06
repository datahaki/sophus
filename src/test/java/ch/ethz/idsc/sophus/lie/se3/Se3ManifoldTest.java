// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import java.util.Arrays;
import java.util.Random;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.bm.IterativeBiinvariantMean;
import ch.ethz.idsc.sophus.bm.MeanDefect;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.TensorMapping;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se3ManifoldTest extends TestCase {
  private static final IterativeBiinvariantMean ITERATIVE_BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.of(Se3Manifold.INSTANCE, Chop._12);
  private static final BarycentricCoordinate[] ALL_COORDINATES = GbcHelper.barycentrics(Se3Manifold.INSTANCE);
  private static final BarycentricCoordinate[] BII_COORDINATES = //
      GbcHelper.biinvariant(Se3Manifold.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se3Group.INSTANCE);

  public void testRandom() {
    Random random = new Random();
    int fails = 0;
    for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES) {
      int n = 7 + random.nextInt(4);
      Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
      Tensor point = TestHelper.spawn_Se3();
      try {
        Tensor weights = barycentricCoordinate.weights(sequence, point);
        AffineQ.require(weights, Chop._08);
        Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
        assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
        Tensor defect = new MeanDefect(sequence, weights, Se3Manifold.INSTANCE.exponential(mean)).tangent();
        Chop._08.requireAllZero(defect);
      } catch (Exception exception) {
        exception.printStackTrace();
        ++fails;
      }
    }
    assertTrue(fails < 5);
  }

  public void testMeanRandom() {
    Distribution distribution = NormalDistribution.of(4, 1);
    for (int n = 7; n < 13; ++n) {
      Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
      Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
      assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
      Tensor defect = new MeanDefect(sequence, weights, Se3Manifold.INSTANCE.exponential(mean)).tangent();
      assertEquals(Dimensions.of(defect), Arrays.asList(2, 3));
      Chop._08.requireAllZero(defect);
    }
  }

  public void testRelativeRandom() {
    Random random = new Random();
    BiinvariantMean biinvariantMean = ITERATIVE_BIINVARIANT_MEAN;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
      int n = 7 + random.nextInt(3);
      int fails = 0;
      try {
        Tensor points = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
        Tensor xya = TestHelper.spawn_Se3();
        Tensor weights = barycentricCoordinate.weights(points, xya);
        AffineQ.require(weights, Chop._08);
        Tensor check1 = biinvariantMean.mean(points, weights);
        Chop._10.requireClose(check1, xya);
        Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights);
        Chop._06.requireClose(xya, x_recreated);
        Tensor shift = TestHelper.spawn_Se3();
        for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
          Tensor all = tensorMapping.slash(points);
          Tensor one = tensorMapping.apply(xya);
          Chop._10.requireClose(one, biinvariantMean.mean(all, weights));
          Chop._05.requireClose(weights, barycentricCoordinate.weights(all, one));
        }
      } catch (Exception exception) {
        exception.printStackTrace();
        ++fails;
      }
      assertTrue(fails < 3);
    }
  }
}
