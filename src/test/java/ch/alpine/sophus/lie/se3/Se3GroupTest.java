// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.so.Rodrigues;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import showcase.GroupCheck;

class Se3GroupTest {
  private static final Exponential LIE_EXPONENTIAL = Se3Group.INSTANCE.exponential0();
  private static final LieGroup LIE_GROUP = Se3Group.INSTANCE;
  private static final RandomSampleInterface RSI_TSe3 = new TSe3RandomSample( //
      UniformDistribution.of(Clips.absolute(10)), //
      TriangularDistribution.with(0, 0.2));
  private static final RandomSampleInterface RSI_Se3 = new Se3RandomSample( //
      UniformDistribution.of(Clips.absolute(10)), //
      TriangularDistribution.with(0, 0.2));

  @Disabled
  @Test
  void testInsync() {
    GroupCheck.check(Se3Group.INSTANCE);
    assertEquals(Se3Group.INSTANCE.toString(), "SE[3]");
  }

  @Test
  void testSimple() {
    Tensor translation = Tensors.vector(1, 2, 3);
    Tensor input = Join.of( //
        translation, //
        Tensors.vector(0.2, 0.3, -0.1));
    Tensor g = Se3Group.INSTANCE.exponential0().exp(input);
    Tensor u_w = Se3Group.INSTANCE.exponential0().log(g);
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
    Tensor g = Se3Group.INSTANCE.exponential0().exp(input);
    Tensor u_w = Se3Group.INSTANCE.exponential0().log(g);
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
    for (int index = 0; index < 10; ++index) {
      Tensor input = RandomVariate.of(distribution, 6);
      Tensor g = Se3Group.INSTANCE.exponential0().exp(input);
      Tensor u_w = Se3Group.INSTANCE.exponential0().log(g);
      Chop._12.requireClose(input, u_w);
    }
  }

  @Test
  void testZero() {
    Tensor input = Join.of( //
        Tensors.vector(1, 2, 3), //
        Tensors.vector(0, 0, 0));
    Tensor g = Se3Group.INSTANCE.exponential0().exp(input);
    assertEquals(g, Se3Matrix.of(IdentityMatrix.of(3), input.extract(0, 3)));
    Tensor u_w = Se3Group.INSTANCE.exponential0().log(g);
    Chop._12.requireClose(input, u_w);
  }

  @Test
  void testAlmostZero() {
    Tensor input = Join.of( //
        Tensors.vector(1, 2, 3), //
        Tensors.vector(1e-15, 1e-15, -1e-15));
    Tensor g = Se3Group.INSTANCE.exponential0().exp(input);
    Tensor u_w = Se3Group.INSTANCE.exponential0().log(g);
    Chop._12.requireClose(input, u_w);
  }

  @Disabled
  @Test
  void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int n = 1; n < 4; ++n)
      for (int count = 0; count < 10; ++count) {
        Tensor g = RandomSample.of(RSI_Se3); // element
        Tensor x = RandomSample.of(RSI_TSe3); // vector
        Tensor lhs = LIE_GROUP.combine(g, LIE_EXPONENTIAL.exp(x)); // g.Exp[x]
        Tensor rhs = LIE_GROUP.combine(LIE_EXPONENTIAL.exp(LIE_GROUP.adjoint(g, x)), g); // Exp[Ad(g).x].g
        Chop._10.requireClose(lhs, rhs);
      }
  }

  @Disabled
  @Test
  void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    for (int n = 1; n < 4; ++n)
      for (int count = 0; count < 10; ++count) {
        Tensor g = RandomSample.of(RSI_Se3); // element
        Tensor m = RandomSample.of(RSI_Se3); // element
        Tensor lhs = LIE_EXPONENTIAL.log( //
            LIE_GROUP.combine(LIE_GROUP.combine(g, m), LIE_GROUP.invert(g))); // Log[g.m.g^-1]
        Tensor rhs = LIE_GROUP.adjoint(g, LIE_EXPONENTIAL.log(m)); // Ad(g).Log[m]
        Chop._10.requireClose(lhs, rhs);
      }
  }

  @Disabled
  @Test
  void testSplit() {
    Tensor p = Se3Group.INSTANCE.exponential0().exp(Join.of(Tensors.vector(1, 2, 3), Tensors.vector(0.2, 0.3, 0.4)));
    Tensor q = Se3Group.INSTANCE.exponential0().exp(Join.of(Tensors.vector(3, 4, 5), Tensors.vector(-0.1, 0.2, 0.2)));
    Tensor split = Se3Group.INSTANCE.split(p, q, RationalScalar.HALF);
    assertTrue(MatrixQ.ofSize(split, 4, 4));
  }

  private static final IterativeBiinvariantMean ITERATIVE_BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.argmax(Se3Group.INSTANCE, Chop._12);
  private static final RandomSampleInterface RSI_Se3A = new Se3RandomSample( //
      UniformDistribution.of(Clips.absolute(5)), //
      TriangularDistribution.with(0, 0.25));

  @Disabled
  @Test
  void testMeanRandom() {
    Distribution distribution = NormalDistribution.of(4, 1);
    for (int n = 7; n < 13; ++n) {
      Tensor sequence = RandomSample.of(RSI_Se3A, n);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
      Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
      assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
      Tensor defect = MeanDefect.of(sequence, weights, Se3Group.INSTANCE.exponential(mean)).tangent();
      assertEquals(Dimensions.of(defect), List.of(6));
      Chop._08.requireAllZero(defect);
    }
  }

  private static void _check(Tensor R, Tensor t) {
    Tensor gt = Se3Matrix.of(R, t);
    Tensor adjoint = Se3Group.INSTANCE.adjoint(Se3Group.INSTANCE.invert(gt), Tensors.vector(1, 2, 3, 4, 5, 6));
    assertEquals(Dimensions.of(adjoint), List.of(6));
    Tensor ge = Se3Group.INSTANCE.combine(gt, IdentityMatrix.of(4));
    Chop._10.requireClose(Se3Matrix.rotation(ge), R);
    Chop._10.requireClose(Se3Matrix.translation(ge), t);
  }

  @Disabled
  @Test
  void testSimpleElem() {
    Tensor R = Rodrigues.vectorExp(Tensors.vector(-1, -.2, .3));
    Tensor t = Tensors.vector(4, 5, 6);
    _check(R, t);
    _check(IdentityMatrix.of(3), Array.zeros(3));
  }
}
