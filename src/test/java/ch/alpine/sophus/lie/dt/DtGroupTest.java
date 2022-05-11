// code by jph
package ch.alpine.sophus.lie.dt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class DtGroupTest {
  private static final LieGroup LIE_GROUP = DtGroup.INSTANCE;

  @Test
  public void testSt1Inverse() {
    Tensor p = Tensors.fromString("{3, {6, 3}}");
    Tensor id = Tensors.fromString("{1, {0, 0}}");
    DtGroupElement pE = DtGroup.INSTANCE.element(p);
    DtGroupElement inv = pE.inverse();
    assertEquals(inv.toCoordinate(), Tensors.fromString("{1/3, {-2, -1}}"));
    assertEquals(inv.combine(p), id);
  }

  @Test
  public void testSt1Combine() {
    Tensor p = Tensors.fromString("{3, {6, 1}}");
    DtGroupElement pE = DtGroup.INSTANCE.element(p);
    Tensor q = Tensors.fromString("{2, {8, 5}}");
    assertEquals(pE.combine(q), Tensors.fromString("{6, {30, 16}}"));
  }

  @Test
  public void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new DtRandomSample(n, ExponentialDistribution.standard(), UniformDistribution.of(-1, 1));
      Tensor g = RandomSample.of(rsi); // element
      RandomSampleInterface tsi = new TDtRandomSample(n, UniformDistribution.of(-1, 1));
      Tensor x = RandomSample.of(tsi); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(LIE_GROUP.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(LIE_GROUP.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  public void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new DtRandomSample(n, ExponentialDistribution.standard(), UniformDistribution.of(-1, 1));
      Tensor g = RandomSample.of(rsi);
      Tensor m = RandomSample.of(rsi);
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_GROUP.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_GROUP.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  public void testSt1Simple() {
    Tensor p = Tensors.vector(5, 1);
    Tensor q = Tensors.vector(10, 0);
    Scalar lambda = RealScalar.of(0.7);
    Tensor split = Tensors.vector(8.122523963562355, 0.37549520728752905);
    Chop._13.requireClose(split, DtGroup.INSTANCE.split(p, q, lambda));
  }

  @Test
  public void testSt1Zero() {
    Tensor split = DtGroup.INSTANCE.split(Tensors.vector(5, 1), Tensors.vector(10, 0), RealScalar.of(0));
    Chop._13.requireClose(split, Tensors.vector(5, 1));
  }

  @Test
  public void testSt1One() {
    Tensor split = DtGroup.INSTANCE.split(Tensors.vector(5, 1), Tensors.vector(10, 0), RealScalar.of(1));
    Chop._13.requireClose(split, Tensors.vector(10, 0));
  }

  @Test
  public void testSt1General() {
    Tensor p = Tensors.vector(3, 6);
    Tensor q = Tensors.vector(4, 10);
    Clip clip_l = Clips.interval(3, 4);
    Clip clip_t = Clips.interval(6, 10);
    for (Tensor x : Subdivide.of(0, 1, 20)) {
      Tensor split = DtGroup.INSTANCE.split(p, q, (Scalar) x);
      clip_l.requireInside(split.Get(0));
      clip_t.requireInside(split.Get(1));
    }
  }

  @Test
  public void testSimple() {
    Tensor p = Tensors.of(RealScalar.of(5), Tensors.vector(1, 0, 2));
    Tensor q = Tensors.of(RealScalar.of(10), Tensors.vector(7, -3, 2));
    Tensor split = DtGroup.INSTANCE.split(p, q, RealScalar.of(0.7));
    Chop._13.requireClose(split, Tensors.of(RealScalar.of(8.122523963562355), Tensors.vector(4.747028756274826, -1.8735143781374128, 2.0)));
  }

  @Test
  public void testZero() {
    Tensor p = Tensors.of(RealScalar.of(5), Tensors.vector(1, 0, 2));
    Tensor q = Tensors.of(RealScalar.of(10), Tensors.vector(7, -3, 2));
    Tensor split = DtGroup.INSTANCE.split(p, q, RealScalar.of(0));
    Chop._13.requireClose(split, p);
  }

  @Test
  public void testOne() {
    Tensor p = Tensors.of(RealScalar.of(5), Tensors.vector(1, 0, 2));
    Tensor q = Tensors.of(RealScalar.of(10), Tensors.vector(7, -3, 2));
    Tensor split = DtGroup.INSTANCE.split(p, q, RealScalar.of(1));
    Chop._13.requireClose(split, q);
  }

  @Test
  public void testGeneral() {
    Tensor p = Tensors.of(RealScalar.of(3), Tensors.vector(6, -2));
    Tensor q = Tensors.of(RealScalar.of(4), Tensors.vector(10, 3));
    Clip clip_l = Clips.interval(3, 4);
    Clip clip_t1 = Clips.interval(6, 10);
    Clip clip_t2 = Clips.interval(-2, 3);
    for (Tensor x : Subdivide.of(0, 1, 20)) {
      Tensor split = DtGroup.INSTANCE.split(p, q, (Scalar) x);
      clip_l.requireInside(split.Get(0));
      clip_t1.requireInside((split.get(1)).Get(0));
      clip_t2.requireInside(split.Get(1, 1));
    }
  }

  @Test
  public void testBiinvariantMean() {
    Tensor p = Tensors.fromString("{1, {2, 3}}");
    Tensor q = Tensors.fromString("{2, {3, 1}}");
    Tensor domain = Subdivide.of(0, 1, 10);
    Tensor st1 = domain.map(DtGroup.INSTANCE.curve(p, q));
    ScalarTensorFunction mean = //
        w -> DtBiinvariantMean.INSTANCE.mean(Tensors.of(p, q), Tensors.of(RealScalar.ONE.subtract(w), w));
    Tensor st2 = domain.map(mean);
    Chop._12.requireClose(st1, st2);
  }
}
