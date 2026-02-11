// code by jph
package ch.alpine.sophus.lie.td;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class TdGroupTest {
  private static final LieGroup LIE_GROUP = TdGroup.INSTANCE;

  @Test
  void testSt1Inverse() {
    Tensor p = Tensors.fromString("{6, 3, 3}");
    Tensor id = Tensors.fromString("{0, 0, 1}");
    Tensor inv = TdGroup.INSTANCE.invert(p);
    assertEquals(inv, Tensors.fromString("{-2, -1, 1/3}"));
    assertEquals(TdGroup.INSTANCE.combine(inv, p), id);
  }

  @Test
  void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), n, ExponentialDistribution.standard());
      Tensor g = RandomSample.of(rsi); // element
      RandomSampleInterface tsi = new TDtRandomSample(UniformDistribution.of(-1, 1), n);
      Tensor x = RandomSample.of(tsi); // vector
      Tensor lhs = LIE_GROUP.combine(g, LIE_GROUP.exponential0().exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.combine(LIE_GROUP.exponential0().exp(LIE_GROUP.adjoint(g, x)), g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @RepeatedTest(10)
  void testAdjointLog(RepetitionInfo repetitionInfo) {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    int n = repetitionInfo.getCurrentRepetition();
    RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), n, ExponentialDistribution.standard());
    Tensor g = RandomSample.of(rsi);
    Tensor m = RandomSample.of(rsi);
    Tensor lhs = LIE_GROUP.exponential0().log( //
        LIE_GROUP.combine(LIE_GROUP.combine(g, m), LIE_GROUP.invert(g))); // Log[g.m.g^-1]
    // LieGroupElement ge = LIE_GROUP.element(g);
    Tensor rhs = LIE_GROUP.adjoint(g, LIE_GROUP.exponential0().log(m)); // Ad(g).Log[m]
    Chop._10.requireClose(lhs, rhs);
  }

  @Test
  void testSt1Simple() {
    Tensor p = Tensors.vector(1, 5);
    Tensor q = Tensors.vector(0, 10);
    Tensor split = Tensors.vector(0.37549520728752905, 8.122523963562355);
    Scalar lambda = RealScalar.of(0.7);
    Chop._13.requireClose(split, TdGroup.INSTANCE.split(p, q, lambda));
  }

  @Test
  void testSt1Zero() {
    Tensor split = TdGroup.INSTANCE.split(Tensors.vector(1, 5), Tensors.vector(0, 10), RealScalar.of(0));
    Chop._13.requireClose(split, Tensors.vector(1, 5));
  }

  @Test
  void testSt1One() {
    Tensor split = TdGroup.INSTANCE.split(Tensors.vector(1, 5), Tensors.vector(0, 10), RealScalar.of(1));
    Chop._13.requireClose(split, Tensors.vector(0, 10));
  }

  @Test
  void testSt1General() {
    Tensor p = Tensors.vector(6, 3);
    Tensor q = Tensors.vector(10, 4);
    Clip clip_t = Clips.interval(6, 10);
    Clip clip_l = Clips.interval(3, 4);
    for (Tensor x : Subdivide.of(0, 1, 20)) {
      Tensor split = TdGroup.INSTANCE.split(p, q, (Scalar) x);
      clip_t.requireInside(split.Get(0));
      clip_l.requireInside(split.Get(1));
    }
  }

  @Test
  void testSimple() {
    Tensor p = Tensors.vector(1, 0, 2, 5);
    Tensor q = Tensors.vector(7, -3, 2, 10);
    Tensor split = TdGroup.INSTANCE.split(p, q, RealScalar.of(0.7));
    Chop._13.requireClose(split, Tensors.vector(4.747028756274826, -1.8735143781374128, 2.0, 8.122523963562355));
  }

  @Test
  void testZero() {
    Tensor p = Tensors.vector(1, 0, 2, 5);
    Tensor q = Tensors.vector(7, -3, 2, 10);
    Tensor split = TdGroup.INSTANCE.split(p, q, RealScalar.of(0));
    Chop._13.requireClose(split, p);
  }

  @Test
  void testOne() {
    Tensor p = Tensors.vector(1, 0, 2, 5);
    Tensor q = Tensors.vector(7, -3, 2, 10);
    Tensor split = TdGroup.INSTANCE.split(p, q, RealScalar.of(1));
    Chop._13.requireClose(split, q);
  }

  @Test
  void testGeneral() {
    Tensor p = Tensors.vector(6, -2, 3);
    Tensor q = Tensors.vector(10, 3, 4);
    Clip clip_t1 = Clips.interval(6, 10);
    Clip clip_t2 = Clips.interval(-2, 3);
    Clip clip_l = Clips.interval(3, 4);
    for (Tensor x : Subdivide.of(0, 1, 20)) {
      Tensor split = TdGroup.INSTANCE.split(p, q, (Scalar) x);
      clip_t1.requireInside(split.Get(0));
      clip_t2.requireInside(split.Get(1));
      clip_l.requireInside(Last.of(split));
    }
  }

  @Test
  void testSt1ExpLogRandom() {
    for (int count = 0; count < 10; ++count) {
      Distribution distribution = NormalDistribution.standard();
      Tensor inp = RandomVariate.of(distribution, 3);
      Tensor xy = TdGroup.INSTANCE.exponential0().exp(inp);
      Tensor uv = TdGroup.INSTANCE.exponential0().log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  @Test
  void testSt1ExpLog() {
    Tensor inp = Tensors.vector(7, 3);
    Tensor xy = TdGroup.INSTANCE.exponential0().exp(inp);
    Tensor uv = TdGroup.INSTANCE.exponential0().log(xy);
    Tolerance.CHOP.requireClose(inp, uv);
  }

  @Test
  void testSt1LogExp() {
    Tensor inp = Tensors.vector(7, 3);
    Tensor uv = TdGroup.INSTANCE.exponential0().log(inp);
    Tensor xy = TdGroup.INSTANCE.exponential0().exp(uv);
    Tolerance.CHOP.requireClose(inp, xy);
  }

  @Test
  void testSt1Singular() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = Tensors.vector(0, Math.random());
      Tensor xy = TdGroup.INSTANCE.exponential0().exp(inp);
      Tensor uv = TdGroup.INSTANCE.exponential0().log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  @Test
  void testExpLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor v = Tensors.vector(Math.random(), Math.random(), -Math.random(), -Math.random());
      Scalar u = RealScalar.of(Math.random());
      Tensor inp = Append.of(v, u);
      Tensor xy = TdGroup.INSTANCE.exponential0().exp(inp);
      Tensor uv = TdGroup.INSTANCE.exponential0().log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  @Test
  void testLogExp() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random(), Math.random());
      Tensor uv = TdGroup.INSTANCE.exponential0().log(inp);
      Tensor xy = TdGroup.INSTANCE.exponential0().exp(uv);
      Tolerance.CHOP.requireClose(inp, xy);
    }
  }

  @Test
  void testSingular() {
    Tensor inp = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random(), 0);
    Tensor xy = TdGroup.INSTANCE.exponential0().exp(inp);
    Tensor uv = TdGroup.INSTANCE.exponential0().log(xy);
    Tolerance.CHOP.requireClose(inp, uv);
  }

  @Test
  void testSt1Inverse44() {
    Tensor p = Tensors.vector(3, 6);
    Tensor id = Tensors.vector(0, 1);
    assertEquals(TdGroup.INSTANCE.diffOp(p).apply(p), id);
  }

  @Test
  void testInverse() {
    Tensor id = Tensors.vector(0, 0, 0, 0, 1);
    for (int count = 0; count < 100; ++count) {
      Scalar lambda = RealScalar.of(Math.random() + 0.001);
      Tensor t = Tensors.vector(Math.random(), 32 * Math.random(), -Math.random(), -17 * Math.random());
      Tensor p = Append.of(t, lambda);
      Chop._11.requireClose(TdGroup.INSTANCE.diffOp(p).apply(p), id);
    }
  }

  // checks that lambda is required to be positive
  @Test
  void testLambdaNonPositiveFail() {
    assertThrows(Exception.class, () -> TdGroup.INSTANCE.requireMember(Tensors.vector(5, 0)));
    assertThrows(Exception.class, () -> TdGroup.INSTANCE.requireMember(Tensors.vector(5, -1)));
  }

  @Test
  void testCombineFail() {
    Tensor p = Tensors.vector(1, 2, 3, 4);
    assertThrows(Exception.class, () -> TdGroup.INSTANCE.combine(p, Tensors.vector(1, 2, 3, 0)));
    assertThrows(Exception.class, () -> TdGroup.INSTANCE.combine(p, Tensors.vector(1, 2, 3, 4, 1)));
  }

  @Test
  void testDlNullFail() {
    Tensor pE = TdGroup.INSTANCE.requireMember(Tensors.vector(1, 2, 3, 4));
    assertThrows(Exception.class, () -> TdGroup.INSTANCE.dL(pE, null));
  }
}
