// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import java.util.Arrays;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.Eigensystem;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Unitize;
import junit.framework.TestCase;

public class Se2CoveringBiinvariantCoordinatesTest extends TestCase {
  private static final ProjectedCoordinate[] BARYCENTRIC_COORDINATES = { //
      HsBiinvariantCoordinate.linear(Se2CoveringManifold.INSTANCE), //
      HsBiinvariantCoordinate.smooth(Se2CoveringManifold.INSTANCE) //
  };

  public void testLagrange() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 4; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      for (ProjectedCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        for (int index = 0; index < n; ++index) {
          Tensor weights = barycentricCoordinate.weights(sequence, sequence.get(index));
          AffineQ.require(weights);
          Tolerance.CHOP.requireClose(weights, UnitVector.of(n, index));
        }
      }
    }
  }

  private static Tensor withUnits(Tensor xya) {
    return Tensors.of( //
        Quantity.of(xya.Get(0), "m"), //
        Quantity.of(xya.Get(1), "m"), //
        xya.Get(2));
  }

  public void testQuantity() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 4; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      sequence.set(Se2CoveringBiinvariantCoordinatesTest::withUnits, Tensor.ALL);
      for (ProjectedCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        for (int index = 0; index < n; ++index) {
          Tensor weights = barycentricCoordinate.weights(sequence, sequence.get(index));
          AffineQ.require(weights);
          Tolerance.CHOP.requireClose(weights, UnitVector.of(n, index));
        }
        Tensor weights = barycentricCoordinate.weights(sequence, withUnits(RandomVariate.of(distribution, 3)));
        AffineQ.require(weights);
      }
    }
  }

  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  public void testProjection() {
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (ProjectedCoordinate biinvariantCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distributiox, n, 3);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor weights1 = biinvariantCoordinate.weights(points, xya);
        Tensor projection = biinvariantCoordinate.projection(points, xya);
        SymmetricMatrixQ.require(projection, Chop._10);
        Chop._10.requireClose(Symmetrize.of(projection), projection);
        AffineQ.require(weights1);
        Tensor check1 = biinvariantMean.mean(points, weights1);
        Chop._10.requireClose(check1, xya);
        Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights1);
        Chop._06.requireClose(xya, x_recreated);
        Tensor shift = TestHelper.spawn_Se2C();
        { // invariant under left action
          Tensor seqlft = LIE_GROUP_OPS.allLeft(points, shift);
          Tensor xyalft = LIE_GROUP_OPS.combine(shift, xya);
          Tensor x_lft = biinvariantMean.mean(seqlft, weights1);
          Chop._10.requireClose(xyalft, x_lft);
          Tensor weightsL = biinvariantCoordinate.weights(seqlft, xyalft);
          Chop._10.requireClose(weights1, weightsL);
          Tensor projL = biinvariantCoordinate.projection(seqlft, xyalft);
          Chop._10.requireClose(projection, projL);
        }
        { // invariant under right action
          Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
          Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
          Tensor weightsR = biinvariantCoordinate.weights(seqrgt, xyargt);
          Tensor x_rgt = biinvariantMean.mean(seqrgt, weightsR);
          Chop._10.requireClose(xyargt, x_rgt);
          Chop._10.requireClose(weights1, weightsR);
          Tensor projR = biinvariantCoordinate.projection(seqrgt, xyargt);
          Chop._10.requireClose(projection, projR);
        }
        { // invariant under inversion
          Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
          Tensor xyainv = LIE_GROUP_OPS.invert(xya);
          Tensor weightsI = biinvariantCoordinate.weights(seqinv, xyainv);
          Tensor check2 = biinvariantMean.mean(seqinv, weightsI);
          Chop._10.requireClose(check2, xyainv);
          AffineQ.require(weightsI);
          Chop._10.requireClose(weights1, weightsI);
          Tensor projI = biinvariantCoordinate.projection(seqinv, xyainv);
          Chop._10.requireClose(projection, projI);
        }
      }
  }

  public void testProjectionIntoAdInvariant() {
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (ProjectedCoordinate projectedCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 4; n < 10; ++n) {
        Tensor sequence = RandomVariate.of(distribution, n, 3);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
        Tensor xya = biinvariantMean.mean(sequence, weights);
        Tensor weights1 = projectedCoordinate.weights(sequence, xya); // projection
        AffineQ.require(weights1);
        Tolerance.CHOP.requireClose(weights, weights);
        Tensor projection = projectedCoordinate.projection(sequence, xya);
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
}
