// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import java.util.Arrays;
import java.util.Random;

import ch.ethz.idsc.sophus.gbc.AveragingWeights;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.gbc.LeveragesCoordinate;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.NormWeighting;
import ch.ethz.idsc.sophus.math.TensorMapping;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.Eigensystem;
import ch.ethz.idsc.tensor.mat.InfluenceMatrix;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.nrm.VectorNorm2Squared;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Unitize;
import junit.framework.TestCase;

public class Se2CoveringManifoldTest extends TestCase {
  private static final BarycentricCoordinate[] ALL_COORDINATES = //
      GbcHelper.barycentrics(Se2CoveringManifold.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  private static final BarycentricCoordinate[] BII_COORDINATES = //
      GbcHelper.biinvariant(Se2CoveringManifold.INSTANCE);
  private static final BarycentricCoordinate[] QUANTITY_COORDINATES = //
      GbcHelper.biinvariant_quantity(Se2CoveringManifold.INSTANCE);
  private static final BarycentricCoordinate AD_INVAR = HsCoordinates.wrap( //
      Se2CoveringManifold.INSTANCE, //
      MetricCoordinate.of( //
          NormWeighting.of(new Se2CoveringTarget(VectorNorm2Squared::of, RealScalar.ONE), InversePowerVariogram.of(1))));

  public void test4Exact() {
    Distribution distribution = UniformDistribution.unit();
    final int n = 4;
    Random random = new Random(1);
    for (int count = 0; count < 5; ++count) {
      Tensor points = RandomVariate.of(distribution, random, n, 3);
      Se2CoveringBarycenter se2CoveringBarycenter = new Se2CoveringBarycenter(points);
      Tensor xya = RandomVariate.of(distribution, random, 3);
      for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES) {
        Tensor weights = barycentricCoordinate.weights(points, xya);
        Chop._06.requireClose(weights, se2CoveringBarycenter.apply(xya));
        Chop._06.requireClose(xya, Se2CoveringBiinvariantMean.INSTANCE.mean(points, weights));
      }
    }
  }

  public void testLinearReproduction() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    int n = 4 + random.nextInt(4);
    Tensor points = RandomVariate.of(distribution, random, n, 3);
    Tensor target = AveragingWeights.of(n);
    Tensor x = Se2CoveringBiinvariantMean.INSTANCE.mean(points, target);
    for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES) {
      Tensor weights = barycentricCoordinate.weights(points, x);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(x, x_recreated);
    }
  }

  public void testRandom() {
    Random random = new Random();
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
      int n = 4 + random.nextInt(4);
      Tensor points = RandomVariate.of(distributiox, n, 3);
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor weights = barycentricCoordinate.weights(points, xya);
      AffineQ.require(weights, Chop._08);
      Tensor check1 = Se2CoveringBiinvariantMean.INSTANCE.mean(points, weights);
      Chop._06.requireClose(check1, xya);
      Chop._06.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(xya, x_recreated);
      Tensor shift = TestHelper.spawn_Se2C();
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor all = tensorMapping.slash(points);
        Tensor one = tensorMapping.apply(xya);
        Chop._06.requireClose(one, biinvariantMean.mean(all, weights));
        Chop._06.requireClose(weights, barycentricCoordinate.weights(all, one));
      }
    }
  }

  public void testNullFail() {
    for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES)
      AssertFail.of(() -> barycentricCoordinate.weights(null, null));
  }

  public void testLagrange() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.standard();
    int n = 4 + random.nextInt(4);
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
      for (int index = 0; index < n; ++index) {
        Tensor weights = barycentricCoordinate.weights(sequence, sequence.get(index));
        AffineQ.require(weights, Chop._08);
        Chop._06.requireClose(weights, UnitVector.of(n, index));
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
    Random random = new Random();
    Distribution distribution = NormalDistribution.standard();
    int n = 4 + random.nextInt(4);
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    sequence.set(Se2CoveringManifoldTest::withUnits, Tensor.ALL);
    for (BarycentricCoordinate barycentricCoordinate : QUANTITY_COORDINATES) {
      for (int index = 0; index < n; ++index) {
        Tensor weights = barycentricCoordinate.weights(sequence, sequence.get(index));
        AffineQ.require(weights, Chop._08);
        Chop._06.requireClose(weights, UnitVector.of(n, index));
      }
      Tensor weights = barycentricCoordinate.weights(sequence, withUnits(RandomVariate.of(distribution, 3)));
      AffineQ.require(weights, Chop._08);
    }
  }

  public void testProjection() {
    Random random = new Random();
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
      int n = 4 + random.nextInt(4);
      Tensor points = RandomVariate.of(distributiox, n, 3);
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor weights = barycentricCoordinate.weights(points, xya);
      Tensor matrix = new HsDesign(vectorLogManifold).matrix(points, xya);
      Tensor influence = matrix.dot(PseudoInverse.of(matrix));
      SymmetricMatrixQ.require(influence, Chop._10);
      Chop._10.requireClose(Symmetrize.of(influence), influence);
      AffineQ.require(weights, Chop._08);
      Tensor check1 = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(check1, xya);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(xya, x_recreated);
      Tensor shift = TestHelper.spawn_Se2C();
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor all = tensorMapping.slash(points);
        Tensor one = tensorMapping.apply(xya);
        Chop._08.requireClose(one, biinvariantMean.mean(all, weights));
        Chop._06.requireClose(weights, barycentricCoordinate.weights(all, one));
        Tensor design = new HsDesign(vectorLogManifold).matrix(all, one);
        Chop._06.requireClose(influence, InfluenceMatrix.of(design).matrix());
      }
    }
  }

  public void testProjectionIntoAdInvariant() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
      int n = 4 + random.nextInt(4);
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
      Tensor xya = biinvariantMean.mean(sequence, weights);
      Tensor weights1 = barycentricCoordinate.weights(sequence, xya); // projection
      AffineQ.require(weights1, Chop._08);
      Chop._08.requireClose(weights, weights);
      Tensor matrix = new HsDesign(vectorLogManifold).matrix(sequence, xya);
      Tensor residualMaker = InfluenceMatrix.of(matrix).residualMaker();
      Chop._08.requireClose(residualMaker.dot(weights), weights);
      assertEquals(Dimensions.of(residualMaker), Arrays.asList(n, n));
      Chop._08.requireClose(Symmetrize.of(residualMaker), residualMaker);
      Eigensystem eigensystem = Eigensystem.ofSymmetric(Symmetrize.of(residualMaker));
      Tensor unitize = Unitize.of(eigensystem.values().map(Tolerance.CHOP));
      Chop._08.requireClose(eigensystem.values(), unitize);
      assertEquals(Total.ofVector(unitize), RealScalar.of(n - 3));
      for (int index = 0; index < n - 3; ++index) {
        Chop._08.requireClose(eigensystem.values().get(index), RealScalar.ONE);
        Tensor eigenw = NormalizeTotal.FUNCTION.apply(eigensystem.vectors().get(index));
        Tensor recons = biinvariantMean.mean(sequence, eigenw);
        Chop._07.requireClose(xya, recons);
      }
    }
  }

  private static final BarycentricCoordinate[] BIINVARIANT_COORDINATES = { //
      // LeveragesCoordinate.slow(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(0)), //
      // LeveragesCoordinate.slow(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(1)), //
      // LeveragesCoordinate.slow(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2)), //
      AD_INVAR };

  public void testA4Exact() {
    // Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    for (BarycentricCoordinate barycentricCoordinate : BIINVARIANT_COORDINATES) {
      int n = 4;
      Tensor points = RandomVariate.of(distribution, n, 3);
      Se2CoveringBarycenter se2CoveringBarycenter = new Se2CoveringBarycenter(points);
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor w1 = barycentricCoordinate.weights(points, xya);
      Tensor w2 = se2CoveringBarycenter.apply(xya);
      Chop._04.requireClose(w1, w2);
      Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(points, w1);
      Chop._04.requireClose(xya, mean);
    }
  }

  public void testALinearReproduction() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BIINVARIANT_COORDINATES) {
      int n = 4 + random.nextInt(4);
      Tensor points = RandomVariate.of(distribution, n, 3);
      Tensor target = AveragingWeights.of(n);
      Tensor x = Se2CoveringBiinvariantMean.INSTANCE.mean(points, target);
      Tensor weights = barycentricCoordinate.weights(points, x);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(x, x_recreated);
    }
  }

  public void testARandom() {
    Random random = new Random();
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BIINVARIANT_COORDINATES) {
      int n = 4 + random.nextInt(4);
      Tensor points = RandomVariate.of(distributiox, n, 3);
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor weights = barycentricCoordinate.weights(points, xya);
      AffineQ.require(weights, Chop._08);
      Tensor check1 = biinvariantMean.mean(points, weights);
      Chop._07.requireClose(check1, xya);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(xya, x_recreated);
      Tensor shift = TestHelper.spawn_Se2C();
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor all = tensorMapping.slash(points);
        Tensor one = tensorMapping.apply(xya);
        Chop._06.requireClose(one, biinvariantMean.mean(all, weights));
        Chop._06.requireClose(weights, barycentricCoordinate.weights(all, one));
      }
    }
  }

  public void testDiagonalNorm() {
    Tensor betas = RandomVariate.of(UniformDistribution.of(1, 2), 4);
    for (Tensor _beta : betas) {
      Scalar beta = (Scalar) _beta;
      // BarycentricCoordinate bc0 = LeveragesCoordinate.slow(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(beta));
      BarycentricCoordinate bc1 = LeveragesCoordinate.of(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(beta));
      for (int n = 4; n < 10; ++n) {
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se2C(), n);
        Tensor mean = TestHelper.spawn_Se2C();
        // Tensor w0 = bc0.weights(sequence, mean);
        // Tensor w1 =
        bc1.weights(sequence, mean);
        // Chop._06.requireClose(w0, w1);
      }
    }
  }

  public void testANullFail() {
    for (BarycentricCoordinate barycentricCoordinate : BIINVARIANT_COORDINATES)
      AssertFail.of(() -> barycentricCoordinate.weights(null, null));
  }
}
