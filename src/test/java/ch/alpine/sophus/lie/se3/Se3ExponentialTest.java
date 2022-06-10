// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class Se3ExponentialTest {
  private static final Exponential LIE_EXPONENTIAL = Se3Group.INSTANCE;
  private static final LieGroup LIE_GROUP = Se3Group.INSTANCE;
  private static final RandomSampleInterface RSI_TSe3 = new TSe3RandomSample( //
      UniformDistribution.of(Clips.absolute(10)), //
      TriangularDistribution.with(0, 0.2));
  private static final RandomSampleInterface RSI_Se3 = new Se3RandomSample( //
      UniformDistribution.of(Clips.absolute(10)), //
      TriangularDistribution.with(0, 0.2));

  @Test
  public void testSimple() {
    Tensor translation = Tensors.vector(1, 2, 3);
    Tensor input = Tensors.of( //
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
  public void testUnits() {
    Tensor input = Tensors.of( //
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
  public void testRandom() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    for (int index = 0; index < 100; ++index) {
      Tensor input = RandomVariate.of(distribution, 2, 3);
      Tensor g = Se3Group.INSTANCE.exp(input);
      Tensor u_w = Se3Group.INSTANCE.log(g);
      Chop._12.requireClose(input, u_w);
    }
  }

  @Test
  public void testZero() {
    Tensor input = Tensors.of( //
        Tensors.vector(1, 2, 3), //
        Tensors.vector(0, 0, 0));
    Tensor g = Se3Group.INSTANCE.exp(input);
    assertEquals(g, Se3Matrix.of(IdentityMatrix.of(3), input.get(0)));
    Tensor u_w = Se3Group.INSTANCE.log(g);
    Chop._12.requireClose(input, u_w);
  }

  @Test
  public void testAlmostZero() {
    Tensor input = Tensors.of( //
        Tensors.vector(1, 2, 3), //
        Tensors.vector(1e-15, 1e-15, -1e-15));
    Tensor g = Se3Group.INSTANCE.exp(input);
    Tensor u_w = Se3Group.INSTANCE.log(g);
    Chop._12.requireClose(input, u_w);
  }

  @Test
  public void testAdjointExp() {
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
  public void testAdjointLog() {
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
}
