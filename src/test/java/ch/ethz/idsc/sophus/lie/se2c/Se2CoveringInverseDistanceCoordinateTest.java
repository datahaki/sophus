// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2CoveringInverseDistanceCoordinateTest extends TestCase {
  static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      // Se2CoveringInverseDistanceCoordinate.INSTANCE, //
      // Se2CoveringInverseDistanceCoordinate.SQUARED, //
      Se2CoveringInverseDistanceCoordinate.BIINVAR //
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
        // System.out.println(w1.map(Round._3));
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
      // System.out.println("n=" + n);
      Tensor points = RandomVariate.of(distribution, n, 3);
      Tensor target = ConstantArray.of(RationalScalar.of(1, n), n);
      Tensor x = Se2CoveringBiinvariantMean.INSTANCE.mean(points, target);
      for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        Tensor weights = barycentricCoordinate.weights(points, x);
        // System.out.println(Norm._2.between(target, weights));
        // System.out.println(weights.map(Round._4));
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
      // System.out.println("n=" + n);
      Tensor points = RandomVariate.of(distributiox, n, 3);
      Tensor xya = RandomVariate.of(distribution, 3);
      for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        // System.out.println("----"+barycentricCoordinate.getClass().getSimpleName());
        Tensor weights1 = barycentricCoordinate.weights(points, xya);
        AffineQ.require(weights1);
        Tensor check1 = Se2CoveringBiinvariantMean.INSTANCE.mean(points, weights1);
        Chop._10.requireClose(check1, xya);
        // System.out.println("f=" + weights1.map(Round._4));
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
        { // invariant under right action
          Tensor seqrgt = LIE_GROUP_OPS.allR(points, shift);
          Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
          Tensor weightsR = barycentricCoordinate.weights(seqrgt, xyargt);
          Tensor x_rgt = biinvariantMean.mean(seqrgt, weightsR);
          Chop._10.requireClose(xyargt, x_rgt);
          // Chop._10.requireClose(weights1, weightsR);
        }
        { // invariant under inversion
          Tensor seqinv = LIE_GROUP_OPS.allI(points);
          Tensor xyainv = LIE_GROUP_OPS.invert(xya);
          Tensor weightsI = barycentricCoordinate.weights(seqinv, xyainv);
          Tensor check2 = Se2CoveringBiinvariantMean.INSTANCE.mean(seqinv, weightsI);
          Chop._10.requireClose(check2, xyainv);
          AffineQ.require(weightsI);
          // Chop._10.requireClose(weights1, weightsI);
        }
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
