// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.dv.AveragingWeights;
import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.dv.GbcHelper;
import ch.alpine.sophus.dv.HsCoordinates;
import ch.alpine.sophus.dv.LeveragesCoordinate;
import ch.alpine.sophus.dv.MetricCoordinate;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.NormWeighting;
import ch.alpine.sophus.math.api.TensorMapping;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
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
import ch.alpine.tensor.nrm.Vector2Norm;
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
import ch.alpine.tensor.sca.pow.Power;

class Se2CoveringGroupTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  private static final RandomSampleInterface RANDOM_SAMPLE_INTERFACE = //
      Se2CoveringRandomSample.uniform(UniformDistribution.of(Clips.absolute(10)));

  @Test
  void testSimple() {
    Se2CoveringGroupElement se2CoveringGroupElement = Se2CoveringGroup.INSTANCE.element(Tensors.vector(1, 2, 3));
    Tensor tensor = se2CoveringGroupElement.combine(Tensors.vector(0, 0, -3));
    assertEquals(tensor, Tensors.vector(1, 2, 0));
  }

  @Test
  void testConvergenceSe2() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Se2CoveringGroup.INSTANCE.exp(x);
    Tensor mY = Se2CoveringGroup.INSTANCE.exp(y);
    Tensor res = Se2CoveringGroup.INSTANCE.log(Se2CoveringGroup.INSTANCE.element(mX).combine(mY));
    Scalar cmp = RealScalar.ONE;
    for (int degree = 1; degree < 6; ++degree) {
      BinaryOperator<Tensor> binaryOperator = Se2Algebra.INSTANCE.bch(degree);
      Tensor z = binaryOperator.apply(x, y);
      Scalar err = Vector2Norm.between(res, z);
      assertTrue(Scalars.lessThan(err, cmp));
      cmp = err;
    }
    Chop._08.requireZero(cmp);
  }

  @Test
  void testAdInv() {
    Random random = new Random();
    int n = 5 + random.nextInt(3);
    Tensor sequence = RandomSample.of(RANDOM_SAMPLE_INTERFACE, n);
    Tensor point = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Tensor shift = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
      Tensor all = tensorMapping.slash(sequence);
      Tensor one = tensorMapping.apply(point);
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.biinvariant(Se2CoveringGroup.INSTANCE)) {
        Tensor w1 = barycentricCoordinate.weights(sequence, point);
        Tensor w2 = barycentricCoordinate.weights(all, one);
        if (!Chop._03.isClose(w1, w2)) {
          System.out.println("---");
          System.out.println(w1);
          System.out.println(w2);
          fail();
        }
      }
      Biinvariant biinvariant = Biinvariants.HARBOR.ofSafe(Se2CoveringGroup.INSTANCE);
      for (int exp = 0; exp < 3; ++exp) {
        Sedarim gr1 = biinvariant.coordinate(Power.function(exp), sequence);
        Sedarim gr2 = biinvariant.coordinate(Power.function(exp), all);
        Tensor w1 = gr1.sunder(point);
        Tensor w2 = gr2.sunder(one);
        Chop._10.requireClose(w1, w2);
      }
    }
  }

  @Test
  void testLinearReproduction() {
    Random random = new Random();
    int n = 5 + random.nextInt(5);
    Tensor sequence = RandomSample.of(RANDOM_SAMPLE_INTERFACE, n);
    Biinvariant biinvariant = Biinvariants.HARBOR.ofSafe(Se2CoveringGroup.INSTANCE);
    Sedarim grCoordinate = biinvariant.coordinate(InversePowerVariogram.of(2), sequence);
    Tensor point = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Tensor weights = grCoordinate.sunder(point);
    Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._05.requireClose(point, mean);
  }

  @Test
  void testArticle() {
    Tensor tensor = Se2CoveringGroup.INSTANCE.split( //
        Tensors.vector(1, 2, 3), Tensors.vector(4, 5, 6), RealScalar.of(0.7));
    Chop._14.requireClose(tensor, Tensors.fromString("{4.483830852817113, 3.2143505344919467, 5.1}"));
  }

  @Test
  void testCenter() {
    // mathematica gives: {2.26033, -0.00147288, 0.981748}
    Tensor p = Tensors.vector(2.017191762967754, -0.08474511292102775, 0.9817477042468103);
    Tensor q = Tensors.vector(2.503476971090440, +0.08179934782700435, 0.9817477042468102);
    Scalar scalar = RealScalar.of(0.5);
    Tensor delta = new Se2CoveringGroupElement(p).inverse().combine(q);
    Tensor x = Se2CoveringGroup.INSTANCE.log(delta).multiply(scalar);
    x.get();
    // x= {0.20432112230000457, -0.1559021143001622, -5.551115123125783E-17}
    // mathematica gives
    // x= {0.204321, .......... -0.155902, ......... -5.55112*10^-17}
    // Tensor exp_x = Se2CoveringIntegrator.INSTANCE.spin(Tensors.vector(0, 0, 0), x);
    // exp_x == {0.20432112230000457, -0.1559021143001622, -5.551115123125783E-17}
    Tensor tensor = Se2CoveringGroup.INSTANCE.split(p, q, scalar);
    // {2.260334367029097, -0.0014728825470118057, 0.9817477042468103}
    Chop._14.requireClose(tensor, //
        Tensors.fromString("{2.260334367029097, -0.0014728825470118057, 0.9817477042468103}"));
  }

  @Test
  void testBiinvariantMean() {
    Distribution distribution = UniformDistribution.of(-3, 8);
    Distribution wd = UniformDistribution.unit();
    int success = 0;
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Scalar w = RandomVariate.of(wd);
      Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(Tensors.of(p, q), Tensors.of(RealScalar.ONE.subtract(w), w));
      Tensor splt = Se2CoveringGroup.INSTANCE.split(p, q, w);
      if (Chop._12.isClose(mean, splt))
        ++success;
    }
    assertTrue(90 < success);
  }

  private static final BarycentricCoordinate[] ALL_COORDINATES = //
      GbcHelper.biinvariant(Se2CoveringGroup.INSTANCE);
  private static final BarycentricCoordinate[] BII_COORDINATES = //
      GbcHelper.biinvariant(Se2CoveringGroup.INSTANCE);
  private static final BarycentricCoordinate[] QUANTITY_COORDINATES = //
      GbcHelper.biinvariant_quantity(Se2CoveringGroup.INSTANCE);
  private static final BarycentricCoordinate AD_INVAR = new HsCoordinates( //
      new HsDesign(Se2CoveringGroup.INSTANCE), //
      new MetricCoordinate( //
          NormWeighting.of(new Se2CoveringTarget(Vector2NormSquared::of, RealScalar.ONE), InversePowerVariogram.of(1))));

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
  void testLinearReproduction2() {
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
    sequence.set(Se2CoveringGroupTest::withUnits, Tensor.ALL);
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
      Tensor unitize = eigensystem.values().map(Tolerance.CHOP).map(Unitize.FUNCTION);
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
