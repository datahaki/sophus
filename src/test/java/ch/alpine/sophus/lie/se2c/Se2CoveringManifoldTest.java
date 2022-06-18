// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.gbc.LeveragesCoordinate;
import ch.alpine.sophus.gbc.MetricCoordinate;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.NormWeighting;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.mat.pi.PseudoInverse;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Unitize;

class Se2CoveringManifoldTest {
  private static final BarycentricCoordinate[] ALL_COORDINATES = //
      GbcHelper.biinvariant(Se2CoveringGroup.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  private static final BarycentricCoordinate[] BII_COORDINATES = //
      GbcHelper.biinvariant(Se2CoveringGroup.INSTANCE);
  private static final BarycentricCoordinate[] QUANTITY_COORDINATES = //
      GbcHelper.biinvariant_quantity(Se2CoveringGroup.INSTANCE);
  private static final BarycentricCoordinate AD_INVAR = new HsCoordinates( //
      new HsDesign(Se2CoveringGroup.INSTANCE), //
      new MetricCoordinate( //
          NormWeighting.of(new Se2CoveringTarget(Vector2NormSquared::of, RealScalar.ONE), InversePowerVariogram.of(1))));
  private static final RandomSampleInterface RANDOM_SAMPLE_INTERFACE = //
      Se2CoveringRandomSample.uniform(UniformDistribution.of(Clips.absolute(10)));

  @Test
  void test4Exact() {
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

  @Test
  void testLinearReproduction() {
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

  @Test
  void testRandom() {
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
      Tensor shift = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor all = tensorMapping.slash(points);
        Tensor one = tensorMapping.apply(xya);
        Chop._06.requireClose(one, biinvariantMean.mean(all, weights));
        Chop._06.requireClose(weights, barycentricCoordinate.weights(all, one));
      }
    }
  }

  @Test
  void testNullFail() {
    for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES)
      assertThrows(Exception.class, () -> barycentricCoordinate.weights(null, null));
  }

  @Test
  void testLagrange() {
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

  @Test
  void testQuantity2() {
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

  @Test
  void testProjection() {
    Random random = new Random();
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
      int n = 4 + random.nextInt(4);
      Tensor points = RandomVariate.of(distributiox, n, 3);
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor weights = barycentricCoordinate.weights(points, xya);
      Tensor matrix = new HsDesign(manifold).matrix(points, xya);
      Tensor influence = matrix.dot(PseudoInverse.of(matrix));
      SymmetricMatrixQ.require(influence, Chop._10);
      Chop._10.requireClose(Symmetrize.of(influence), influence);
      AffineQ.require(weights, Chop._08);
      Tensor check1 = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(check1, xya);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(xya, x_recreated);
      Tensor shift = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor all = tensorMapping.slash(points);
        Tensor one = tensorMapping.apply(xya);
        Chop._08.requireClose(one, biinvariantMean.mean(all, weights));
        Chop._06.requireClose(weights, barycentricCoordinate.weights(all, one));
        Tensor design = new HsDesign(manifold).matrix(all, one);
        Chop._06.requireClose(influence, InfluenceMatrix.of(design).matrix());
      }
    }
  }

  @Test
  void testProjectionIntoAdInvariant() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
      int n = 4 + random.nextInt(4);
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
      Tensor xya = biinvariantMean.mean(sequence, weights);
      Tensor weights1 = barycentricCoordinate.weights(sequence, xya); // projection
      AffineQ.require(weights1, Chop._08);
      Chop._08.requireClose(weights, weights);
      Tensor matrix = new HsDesign(manifold).matrix(sequence, xya);
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

  @Test
  void testA4Exact() {
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

  @Test
  void testALinearReproduction() {
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

  @Test
  void testARandom() {
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
      Tensor shift = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor all = tensorMapping.slash(points);
        Tensor one = tensorMapping.apply(xya);
        Chop._06.requireClose(one, biinvariantMean.mean(all, weights));
        Chop._06.requireClose(weights, barycentricCoordinate.weights(all, one));
      }
    }
  }

  @Test
  void testDiagonalNorm() {
    Tensor betas = RandomVariate.of(UniformDistribution.of(1, 2), 4);
    for (Tensor _beta : betas) {
      Scalar beta = (Scalar) _beta;
      // BarycentricCoordinate bc0 = LeveragesCoordinate.slow(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(beta));
      BarycentricCoordinate bc1 = LeveragesCoordinate.of(new HsDesign(Se2CoveringGroup.INSTANCE), InversePowerVariogram.of(beta));
      for (int n = 4; n < 10; ++n) {
        Tensor sequence = RandomSample.of(RANDOM_SAMPLE_INTERFACE, n);
        Tensor mean = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
        // Tensor w0 = bc0.weights(sequence, mean);
        // Tensor w1 =
        bc1.weights(sequence, mean);
        // Chop._06.requireClose(w0, w1);
      }
    }
  }

  private static final LieGroup LIE_GROUP = Se2CoveringGroup.INSTANCE;

  @Test
  void testSimpleXY() {
    Tensor x = Tensors.vector(3, 2, 0).unmodifiable();
    Tensor g = LIE_GROUP.exp(x);
    ExactTensorQ.require(g);
    assertEquals(g, x);
    Tensor y = LIE_GROUP.log(g);
    ExactTensorQ.require(y);
    assertEquals(y, x);
  }

  @Test
  void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3); // element
      Tensor x = RandomVariate.of(distribution, 3); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(LIE_GROUP.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(LIE_GROUP.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, random, 3); // element
      Tensor m = RandomVariate.of(distribution, random, 3); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_GROUP.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_GROUP.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testSimpleLinearSubspace() {
    for (int theta = -10; theta <= 10; ++theta) {
      Tensor x = Tensors.vector(0, 0, theta).unmodifiable();
      Tensor g = Se2CoveringGroup.INSTANCE.exp(x);
      assertEquals(g, x);
      Tensor y = Se2CoveringGroup.INSTANCE.log(g);
      assertEquals(y, x);
    }
  }

  @Test
  void testQuantity() {
    Tensor xya = Tensors.fromString("{1[m], 2[m], 0.3}");
    Tensor log = LIE_GROUP.log(xya);
    Chop._12.requireClose(log, Tensors.fromString("{1.2924887258384925[m], 1.834977451676985[m], 0.3}"));
    Tensor exp = LIE_GROUP.exp(log);
    Chop._12.requireClose(exp, xya);
  }

  @Test
  void testANullFail() {
    for (BarycentricCoordinate barycentricCoordinate : BIINVARIANT_COORDINATES)
      assertThrows(Exception.class, () -> barycentricCoordinate.weights(null, null));
  }
}
