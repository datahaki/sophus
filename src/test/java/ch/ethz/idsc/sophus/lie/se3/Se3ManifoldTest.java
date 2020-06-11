// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import java.util.Arrays;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se3ManifoldTest extends TestCase {
  private static final IterativeBiinvariantMean ITERATIVE_BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.of(Se3Manifold.HS_EXP);
  public static final MeanDefect MEAN_DEFECT = BiinvariantMeanDefect.of(Se3Manifold.HS_EXP);
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = GbcHelper.barycentrics(Se3Manifold.INSTANCE);
  private static final BarycentricCoordinate[] REL_BARYCENTRIC_COORDINATES = //
      GbcHelper.relatives(Se3Manifold.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se3Group.INSTANCE);

  public void testRandom() {
    int fails = 0;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 7; n < 13; ++n) {
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
        Tensor point = TestHelper.spawn_Se3();
        try {
          Tensor weights = barycentricCoordinate.weights(sequence, point);
          AffineQ.require(weights);
          Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
          assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
          Tensor defect = MEAN_DEFECT.defect(sequence, weights, mean);
          Chop._08.requireAllZero(defect);
        } catch (Exception exception) {
          ++fails;
        }
      }
    assertTrue(fails < 5);
  }

  public void testMeanRandom() {
    Distribution distributiont = NormalDistribution.of(4, 1);
    for (int count = 0; count < 10; ++count)
      for (int n = 7; n < 13; ++n) {
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distributiont, n));
        Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
        assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
        Tensor defect = MEAN_DEFECT.defect(sequence, weights, mean);
        assertEquals(Dimensions.of(defect), Arrays.asList(2, 3));
        Chop._08.requireAllZero(defect);
      }
  }

  public void testRelativeRandom() {
    BiinvariantMean biinvariantMean = ITERATIVE_BIINVARIANT_MEAN;
    for (BarycentricCoordinate barycentricCoordinate : REL_BARYCENTRIC_COORDINATES)
      for (int n = 7; n < 11; ++n) {
        int fails = 0;
        try {
          Tensor points = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
          Tensor xya = TestHelper.spawn_Se3();
          Tensor weights1 = barycentricCoordinate.weights(points, xya);
          AffineQ.require(weights1);
          Tensor check1 = biinvariantMean.mean(points, weights1);
          Chop._10.requireClose(check1, xya);
          Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
          Tensor x_recreated = biinvariantMean.mean(points, weights1);
          Chop._06.requireClose(xya, x_recreated);
          Tensor shift = TestHelper.spawn_Se3();
          { // invariant under left action
            Tensor seqlft = LIE_GROUP_OPS.allLeft(points, shift);
            Tensor xyalft = LIE_GROUP_OPS.combine(shift, xya);
            Tensor x_lft = biinvariantMean.mean(seqlft, weights1);
            Chop._10.requireClose(xyalft, x_lft);
            Tensor weightsL = barycentricCoordinate.weights(seqlft, xyalft);
            Chop._05.requireClose(weights1, weightsL);
          }
          { // invariant under right action
            Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
            Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
            Tensor weightsR = barycentricCoordinate.weights(seqrgt, xyargt);
            Tensor x_rgt = biinvariantMean.mean(seqrgt, weightsR);
            Chop._10.requireClose(xyargt, x_rgt);
            Chop._05.requireClose(weights1, weightsR);
          }
          { // invariant under inversion
            Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
            Tensor xyainv = LIE_GROUP_OPS.invert(xya);
            Tensor weightsI = barycentricCoordinate.weights(seqinv, xyainv);
            Tensor check2 = biinvariantMean.mean(seqinv, weightsI);
            Chop._10.requireClose(check2, xyainv);
            AffineQ.require(weightsI);
            Chop._05.requireClose(weights1, weightsI);
          }
        } catch (Exception exception) {
          ++fails;
        }
        assertTrue(fails < 3);
      }
  }
}
