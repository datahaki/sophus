// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import java.util.Arrays;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.LieBarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.Eigensystem;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Unitize;
import junit.framework.TestCase;

public class Se2CoveringInverseDistanceCoordinateTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      Se2CoveringInverseDistanceCoordinate.INSTANCE, //
      Se2CoveringInverseDistanceCoordinate.SQUARED //
  };

  public void test4Exact() {
    Distribution distribution = UniformDistribution.unit();
    final int n = 4;
    for (int count = 0; count < 10; ++count) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Se2CoveringBarycenter se2CoveringBarycenter = new Se2CoveringBarycenter(points);
      Tensor xya = RandomVariate.of(distribution, 3);
      for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        Tensor w1 = barycentricCoordinate.weights(points, xya);
        Tensor w2 = se2CoveringBarycenter.apply(xya);
        Chop._06.requireClose(w1, w2);
        Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(points, w1);
        Chop._06.requireClose(xya, mean);
      }
    }
  }

  public void testLinearReproduction() {
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (int n = 4; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Tensor target = ConstantArray.of(RationalScalar.of(1, n), n);
      Tensor x = Se2CoveringBiinvariantMean.INSTANCE.mean(points, target);
      for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        Tensor weights = barycentricCoordinate.weights(points, x);
        Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights);
        Chop._06.requireClose(x, x_recreated);
      }
    }
  }

  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  public void testRandom() {
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (int n = 4; n < 10; ++n) {
      Tensor points = RandomVariate.of(distributiox, n, 3);
      Tensor xya = RandomVariate.of(distribution, 3);
      for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        Tensor weights1 = barycentricCoordinate.weights(points, xya);
        AffineQ.require(weights1);
        Tensor check1 = Se2CoveringBiinvariantMean.INSTANCE.mean(points, weights1);
        Chop._10.requireClose(check1, xya);
        Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights1);
        Chop._06.requireClose(xya, x_recreated);
        Tensor shift = TestHelper.spawn_Se2C();
        { // invariant under left action
          Tensor seqlft = LIE_GROUP_OPS.allL(points, shift);
          Tensor xyalft = LIE_GROUP_OPS.combine(shift, xya);
          Tensor x_lft = biinvariantMean.mean(seqlft, weights1);
          Chop._10.requireClose(xyalft, x_lft);
          Tensor weightsL = barycentricCoordinate.weights(seqlft, xyalft);
          Chop._10.requireClose(weights1, weightsL);
        }
        { // result invariant under right action
          Tensor seqrgt = LIE_GROUP_OPS.allR(points, shift);
          Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
          Tensor weightsR = barycentricCoordinate.weights(seqrgt, xyargt);
          Chop._10.requireClose(weights1, weightsR);
          Tensor x_rgt = biinvariantMean.mean(seqrgt, weightsR);
          Chop._10.requireClose(xyargt, x_rgt);
        }
        { // result invariant under inversion
          Tensor seqinv = LIE_GROUP_OPS.allI(points);
          Tensor xyainv = LIE_GROUP_OPS.invert(xya);
          Tensor weightsI = barycentricCoordinate.weights(seqinv, xyainv);
          Chop._10.requireClose(weights1, weightsI);
          Tensor check2 = Se2CoveringBiinvariantMean.INSTANCE.mean(seqinv, weightsI);
          Chop._10.requireClose(check2, xyainv);
          AffineQ.require(weightsI);
        }
      }
    }
  }

  public void testProjectionIntoAdInvariant() {
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (int n = 4; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
      Tensor xya = biinvariantMean.mean(sequence, weights);
      LieBarycentricCoordinate lieBarycentricCoordinate = new LieBarycentricCoordinate( //
          Se2CoveringGroup.INSTANCE, //
          Se2CoveringExponential.INSTANCE::log, //
          target -> weights);
      Tensor weights1 = lieBarycentricCoordinate.weights(sequence, xya); // projection
      AffineQ.require(weights1);
      Tolerance.CHOP.requireClose(weights, weights);
      Tensor projection = lieBarycentricCoordinate.projection(sequence, xya);
      Tolerance.CHOP.requireClose(projection.dot(weights), weights);
      assertEquals(Dimensions.of(projection), Arrays.asList(n, n));
      Tolerance.CHOP.requireClose(Symmetrize.of(projection), projection);
      Eigensystem eigensystem = Eigensystem.ofSymmetric(Symmetrize.of(projection));
      Tensor unitize = Unitize.of(eigensystem.values().map(Tolerance.CHOP));
      Tolerance.CHOP.requireClose(eigensystem.values(), unitize);
      assertEquals(Total.ofVector(unitize), RealScalar.of(n - 3));
      for (int index = 0; index < n - 3; ++index) {
        Chop._12.requireClose(eigensystem.values().get(index), RealScalar.ONE);
        Tensor eigenw = NormalizeTotal.FUNCTION.apply(eigensystem.vectors().get(index));
        Tensor recons = biinvariantMean.mean(sequence, eigenw);
        Chop._07.requireClose(xya, recons);
      }
    }
  }

  public void testNullFail() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      try {
        barycentricCoordinate.weights(null, null);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }
}
