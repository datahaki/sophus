// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.GbcHelper;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.api.TensorMapping;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class Se3GroupTest {
  private static final Exponential LIE_EXPONENTIAL = Se3Group.INSTANCE;
  private static final LieGroup LIE_GROUP = Se3Group.INSTANCE;
  private static final RandomSampleInterface RSI_TSe3 = new TSe3RandomSample( //
      UniformDistribution.of(Clips.absolute(10)), //
      TriangularDistribution.with(0, 0.2));
  private static final RandomSampleInterface RSI_Se3 = new Se3RandomSample( //
      UniformDistribution.of(Clips.absolute(10)), //
      TriangularDistribution.with(0, 0.2));

  @Test
  void testSimple() {
    Tensor translation = Tensors.vector(1, 2, 3);
    Tensor input = Join.of( //
        translation, //
        Tensors.vector(0.2, 0.3, -0.1));
    Tensor g = Se3Group.INSTANCE.exp(input);
    Tensor u_w = Se3Group.INSTANCE.log(g);
    Chop._12.requireClose(input, u_w);
    Tensor log = MatrixLog.of(g);
    Chop._12.requireClose(Se3Matrix.translation(log), translation);
    Tensor exp = MatrixExp.of(log);
    Chop._12.requireClose(g, exp);
  }

  @Test
  void testUnits() {
    Tensor input = Join.of( //
        Tensors.fromString("{1[m*s^-1], 2[m*s^-1], 3[m*s^-1]}"), //
        Tensors.vector(0.2, 0.3, -0.1));
    Tensor g = Se3Group.INSTANCE.exp(input);
    Tensor u_w = Se3Group.INSTANCE.log(g);
    Chop._12.requireClose(input, u_w);
  }
  // public void testUnits2() {
  // Tensor input = Tensors.of( //
  // Tensors.fromString("{1[m*s^-1], 2[m*s^-1], 3[m*s^-1]}"), //
  // Tensors.fromString("{0.2[s^-1], 0.3[s^-1], -0.1[s^-1]}"));
  // Tensor g = Se3Exponential.INSTANCE.exp(input);
  // Tensor u_w = Se3Exponential.INSTANCE.log(g);
  // Chop._12.requireClose(input, u_w);
  // }

  @Test
  void testRandom() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    for (int index = 0; index < 100; ++index) {
      Tensor input = RandomVariate.of(distribution, 6);
      Tensor g = Se3Group.INSTANCE.exp(input);
      Tensor u_w = Se3Group.INSTANCE.log(g);
      Chop._12.requireClose(input, u_w);
    }
  }

  @Test
  void testZero() {
    Tensor input = Join.of( //
        Tensors.vector(1, 2, 3), //
        Tensors.vector(0, 0, 0));
    Tensor g = Se3Group.INSTANCE.exp(input);
    assertEquals(g, Se3Matrix.of(IdentityMatrix.of(3), input.extract(0, 3)));
    Tensor u_w = Se3Group.INSTANCE.log(g);
    Chop._12.requireClose(input, u_w);
  }

  @Test
  void testAlmostZero() {
    Tensor input = Join.of( //
        Tensors.vector(1, 2, 3), //
        Tensors.vector(1e-15, 1e-15, -1e-15));
    Tensor g = Se3Group.INSTANCE.exp(input);
    Tensor u_w = Se3Group.INSTANCE.log(g);
    Chop._12.requireClose(input, u_w);
  }

  @Test
  void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int n = 1; n < 4; ++n)
      for (int count = 0; count < 10; ++count) {
        Tensor g = RandomSample.of(RSI_Se3); // element
        Tensor x = RandomSample.of(RSI_TSe3); // vector
        LieGroupElement ge = LIE_GROUP.element(g);
        Tensor lhs = ge.combine(LIE_EXPONENTIAL.exp(x)); // g.Exp[x]
        Tensor rhs = LIE_GROUP.element(LIE_EXPONENTIAL.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
        Chop._10.requireClose(lhs, rhs);
      }
  }

  @Test
  void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    for (int n = 1; n < 4; ++n)
      for (int count = 0; count < 10; ++count) {
        Tensor g = RandomSample.of(RSI_Se3); // element
        Tensor m = RandomSample.of(RSI_Se3); // element
        LieGroupElement ge = LIE_GROUP.element(g);
        Tensor lhs = LIE_EXPONENTIAL.log( //
            LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
        Tensor rhs = ge.adjoint(LIE_EXPONENTIAL.log(m)); // Ad(g).Log[m]
        Chop._10.requireClose(lhs, rhs);
      }
  }

  @Test
  void testSimple2() {
    Se3Group.INSTANCE.element(IdentityMatrix.of(4));
  }

  @Test
  void testSplit() {
    Tensor p = Se3Group.INSTANCE.exp(Join.of(Tensors.vector(1, 2, 3), Tensors.vector(0.2, 0.3, 0.4)));
    Tensor q = Se3Group.INSTANCE.exp(Join.of(Tensors.vector(3, 4, 5), Tensors.vector(-0.1, 0.2, 0.2)));
    Tensor split = Se3Group.INSTANCE.split(p, q, RationalScalar.HALF);
    assertTrue(MatrixQ.ofSize(split, 4, 4));
  }

  private static final IterativeBiinvariantMean ITERATIVE_BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.argmax(Se3Group.INSTANCE, Chop._12);
  private static final BarycentricCoordinate[] ALL_COORDINATES = GbcHelper.biinvariant(Se3Group.INSTANCE);
  private static final BarycentricCoordinate[] BII_COORDINATES = //
      GbcHelper.biinvariant(Se3Group.INSTANCE);
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se3Group.INSTANCE);
  private static final RandomSampleInterface RSI_Se3A = new Se3RandomSample( //
      UniformDistribution.of(Clips.absolute(5)), //
      TriangularDistribution.with(0, 0.25));

  @Test
  void testRandom2() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : ALL_COORDINATES) {
      int n = 7 + random.nextInt(4);
      Tensor sequence = RandomSample.of(RSI_Se3A, random, n);
      Tensor point = RandomSample.of(RSI_Se3A, random);
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
      Tensor sequence = RandomSample.of(RSI_Se3A, n);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
      Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
      assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
      Tensor defect = new MeanDefect(sequence, weights, Se3Group.INSTANCE.exponential(mean)).tangent();
      assertEquals(Dimensions.of(defect), List.of(6));
      Chop._08.requireAllZero(defect);
    }
  }

  @Test
  void testRelativeRandom() {
    Random random = new Random(3);
    BiinvariantMean biinvariantMean = ITERATIVE_BIINVARIANT_MEAN;
    for (BarycentricCoordinate barycentricCoordinate : BII_COORDINATES) {
      int n = 7 + random.nextInt(3);
      Tensor points = RandomSample.of(RSI_Se3A, random, n);
      Tensor xya = RandomSample.of(RSI_Se3A, random);
      Tensor weights = barycentricCoordinate.weights(points, xya);
      AffineQ.require(weights, Chop._08);
      Tensor check1 = biinvariantMean.mean(points, weights);
      Chop._10.requireClose(check1, xya);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(xya, x_recreated);
      Tensor shift = RandomSample.of(RSI_Se3A, random);
      for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
        Tensor all = tensorMapping.slash(points);
        Tensor one = tensorMapping.apply(xya);
        Chop._10.requireClose(one, biinvariantMean.mean(all, weights));
        Chop._05.requireClose(weights, barycentricCoordinate.weights(all, one));
      }
    }
  }

  @Test
  void testVectorFail() {
    assertThrows(Exception.class, () -> Se3Group.INSTANCE.element(UnitVector.of(4, 1)));
  }

  @Test
  void testMatrixFail() {
    assertThrows(Exception.class, () -> Se3Group.INSTANCE.element(HilbertMatrix.of(3)));
  }
}
