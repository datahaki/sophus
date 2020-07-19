// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import java.util.Arrays;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.gbc.LeverageCoordinate;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.HsInfluence;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.NormWeighting;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
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

public class Se2CoveringManifoldTest extends TestCase {
  private static final BarycentricCoordinate[] ALL_COORDINATES = //
      GbcHelper.barycentrics(Se2CoveringManifold.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  private static final BarycentricCoordinate[] BII_COORDINATES = //
      GbcHelper.biinvariant(Se2CoveringManifold.INSTANCE);
  private static final BarycentricCoordinate[] QUANTITY_COORDINATES = //
      GbcHelper.biinvariant_quantity(Se2CoveringManifold.INSTANCE);
  private static final BarycentricCoordinate AD_INVAR = MetricCoordinate.custom( //
      Se2CoveringManifold.INSTANCE, //
      NormWeighting.of(new Se2CoveringTarget(RnNormSquared.INSTANCE, RealScalar.ONE), InversePowerVariogram.of(1)));

  public void test4Exact() {
    Distribution distribution = UniformDistribution.unit();
    final int n = 4;
    for (int count = 0; count < 10; ++count) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Se2CoveringBarycenter se2CoveringBarycenter = new Se2CoveringBarycenter(points);
      Tensor xya = RandomVariate.of(distribution, 3);
      for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES) {
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
      for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES) {
        Tensor weights = barycentricCoordinate.weights(points, x);
        Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights);
        Chop._06.requireClose(x, x_recreated);
      }
    }
  }

  public void testRandom() {
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES)
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distributiox, n, 3);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor weights1 = barycentricCoordinate.weights(points, xya);
        AffineQ.require(weights1);
        Tensor check1 = Se2CoveringBiinvariantMean.INSTANCE.mean(points, weights1);
        Chop._06.requireClose(check1, xya);
        Chop._06.requireClose(Total.ofVector(weights1), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights1);
        Chop._06.requireClose(xya, x_recreated);
        Tensor shift = TestHelper.spawn_Se2C();
        { // invariant under left action
          Tensor seqlft = LIE_GROUP_OPS.allLeft(points, shift);
          Tensor xyalft = LIE_GROUP_OPS.combine(shift, xya);
          Tensor x_lft = biinvariantMean.mean(seqlft, weights1);
          Chop._06.requireClose(xyalft, x_lft);
          Tensor weightsL = barycentricCoordinate.weights(seqlft, xyalft);
          Chop._06.requireClose(weights1, weightsL);
        }
        { // result invariant under right action
          Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
          Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
          Tensor weightsR = barycentricCoordinate.weights(seqrgt, xyargt);
          Tensor x_rgt = biinvariantMean.mean(seqrgt, weightsR);
          Chop._06.requireClose(xyargt, x_rgt);
        }
        { // result invariant under inversion
          Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
          Tensor xyainv = LIE_GROUP_OPS.invert(xya);
          Tensor weightsI = barycentricCoordinate.weights(seqinv, xyainv);
          Tensor check2 = Se2CoveringBiinvariantMean.INSTANCE.mean(seqinv, weightsI);
          Chop._06.requireClose(check2, xyainv);
          AffineQ.require(weightsI);
        }
      }
  }

  public void testNullFail() {
    for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES)
      try {
        barycentricCoordinate.weights(null, null);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }

  public void testLagrange() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 4; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
        for (int index = 0; index < n; ++index) {
          Tensor weights = barycentricCoordinate.weights(sequence, sequence.get(index));
          AffineQ.require(weights);
          Chop._06.requireClose(weights, UnitVector.of(n, index));
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
      sequence.set(Se2CoveringManifoldTest::withUnits, Tensor.ALL);
      for (BarycentricCoordinate barycentricCoordinate : QUANTITY_COORDINATES) {
        for (int index = 0; index < n; ++index) {
          Tensor weights = barycentricCoordinate.weights(sequence, sequence.get(index));
          AffineQ.require(weights);
          Chop._06.requireClose(weights, UnitVector.of(n, index));
        }
        Tensor weights = barycentricCoordinate.weights(sequence, withUnits(RandomVariate.of(distribution, 3)));
        AffineQ.require(weights);
      }
    }
  }

  public void testProjection() {
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES)
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distributiox, n, 3);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor weights1 = barycentricCoordinate.weights(points, xya);
        Tensor influence = new HsInfluence(vectorLogManifold.logAt(xya), points).matrix();
        SymmetricMatrixQ.require(influence, Chop._10);
        Chop._10.requireClose(Symmetrize.of(influence), influence);
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
          Chop._08.requireClose(xyalft, x_lft);
          Tensor weightsL = barycentricCoordinate.weights(seqlft, xyalft);
          Chop._06.requireClose(weights1, weightsL);
          Tensor projL = new HsInfluence(vectorLogManifold.logAt(xyalft), seqlft).matrix();
          Chop._06.requireClose(influence, projL);
        }
        { // invariant under right action
          Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
          Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
          Tensor weightsR = barycentricCoordinate.weights(seqrgt, xyargt);
          Tensor x_rgt = biinvariantMean.mean(seqrgt, weightsR);
          Chop._08.requireClose(xyargt, x_rgt);
          Chop._06.requireClose(weights1, weightsR);
          Tensor projR = new HsInfluence(vectorLogManifold.logAt(xyargt), seqrgt).matrix();
          Chop._10.requireClose(influence, projR);
        }
        { // invariant under inversion
          Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
          Tensor xyainv = LIE_GROUP_OPS.invert(xya);
          Tensor weightsI = barycentricCoordinate.weights(seqinv, xyainv);
          Tensor check2 = biinvariantMean.mean(seqinv, weightsI);
          Chop._10.requireClose(check2, xyainv);
          AffineQ.require(weightsI);
          Chop._06.requireClose(weights1, weightsI);
          Tensor projI = new HsInfluence(vectorLogManifold.logAt(xyainv), seqinv).matrix();
          Chop._10.requireClose(influence, projI);
        }
      }
  }

  public void testProjectionIntoAdInvariant() {
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES)
      for (int n = 4; n < 10; ++n) {
        Tensor sequence = RandomVariate.of(distribution, n, 3);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
        Tensor xya = biinvariantMean.mean(sequence, weights);
        Tensor weights1 = barycentricCoordinate.weights(sequence, xya); // projection
        AffineQ.require(weights1);
        Chop._08.requireClose(weights, weights);
        Tensor residualMaker = new HsInfluence(vectorLogManifold.logAt(xya), sequence).residualMaker();
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
      LeverageCoordinate.slow(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(0)), //
      LeverageCoordinate.slow(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(1)), //
      LeverageCoordinate.slow(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2)), //
      AD_INVAR };

  public void testA4Exact() {
    Distribution distribution = UniformDistribution.unit();
    for (BarycentricCoordinate barycentricCoordinate : BIINVARIANT_COORDINATES) {
      final int n = 4;
      for (int count = 0; count < 10; ++count) {
        Tensor points = RandomVariate.of(distribution, n, 3);
        Se2CoveringBarycenter se2CoveringBarycenter = new Se2CoveringBarycenter(points);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor w1 = barycentricCoordinate.weights(points, xya);
        Tensor w2 = se2CoveringBarycenter.apply(xya);
        Chop._06.requireClose(w1, w2);
        Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(points, w1);
        Chop._06.requireClose(xya, mean);
      }
    }
  }

  public void testALinearReproduction() {
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BIINVARIANT_COORDINATES)
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, 3);
        Tensor target = ConstantArray.of(RationalScalar.of(1, n), n);
        Tensor x = Se2CoveringBiinvariantMean.INSTANCE.mean(points, target);
        Tensor weights = barycentricCoordinate.weights(points, x);
        Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights);
        Chop._06.requireClose(x, x_recreated);
      }
  }

  public void testARandom() {
    Distribution distributiox = NormalDistribution.standard();
    Distribution distribution = NormalDistribution.of(0, 0.1);
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BIINVARIANT_COORDINATES)
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distributiox, n, 3);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor weights1 = barycentricCoordinate.weights(points, xya);
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
          Tensor weightsL = barycentricCoordinate.weights(seqlft, xyalft);
          Chop._10.requireClose(weights1, weightsL);
        }
        { // invariant under right action
          Tensor seqrgt = LIE_GROUP_OPS.allRight(points, shift);
          Tensor xyargt = LIE_GROUP_OPS.combine(xya, shift);
          Tensor weightsR = barycentricCoordinate.weights(seqrgt, xyargt);
          Tensor x_rgt = biinvariantMean.mean(seqrgt, weightsR);
          Chop._10.requireClose(xyargt, x_rgt);
          Chop._06.requireClose(weights1, weightsR);
        }
        { // invariant under inversion
          Tensor seqinv = LIE_GROUP_OPS.allInvert(points);
          Tensor xyainv = LIE_GROUP_OPS.invert(xya);
          Tensor weightsI = barycentricCoordinate.weights(seqinv, xyainv);
          Tensor check2 = biinvariantMean.mean(seqinv, weightsI);
          Chop._10.requireClose(check2, xyainv);
          AffineQ.require(weightsI);
          Chop._10.requireClose(weights1, weightsI);
        }
      }
  }

  public void testDiagonalNorm() {
    Tensor betas = RandomVariate.of(UniformDistribution.of(1, 2), 4);
    for (Tensor beta_ : betas) {
      Scalar beta = beta_.Get();
      BarycentricCoordinate bc0 = LeverageCoordinate.slow(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(beta));
      BarycentricCoordinate bc1 = LeverageCoordinate.of(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(beta));
      for (int n = 4; n < 10; ++n) {
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se2C(), n);
        Tensor mean = TestHelper.spawn_Se2C();
        Tensor w0 = bc0.weights(sequence, mean);
        Tensor w1 = bc1.weights(sequence, mean);
        Chop._06.requireClose(w0, w1);
      }
    }
  }

  public void testANullFail() {
    for (BarycentricCoordinate barycentricCoordinate : BIINVARIANT_COORDINATES)
      try {
        barycentricCoordinate.weights(null, null);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }
}
