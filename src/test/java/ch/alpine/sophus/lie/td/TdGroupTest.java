// code by jph
package ch.alpine.sophus.lie.td;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.gbc.AffineWrap;
import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
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
    TdGroupElement pE = TdGroup.INSTANCE.element(p);
    TdGroupElement inv = pE.inverse();
    assertEquals(inv.toCoordinate(), Tensors.fromString("{-2, -1, 1/3}"));
    assertEquals(inv.combine(p), id);
  }

  @Test
  void testSt1Combine() {
    Tensor p = Tensors.vector(6, 1, 3);
    TdGroupElement pE = TdGroup.INSTANCE.element(p);
    Tensor q = Tensors.vector(8, 5, 2);
    assertEquals(pE.combine(q), Tensors.vector(30, 16, 6));
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
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), n, ExponentialDistribution.standard());
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
      Tensor xy = TdGroup.INSTANCE.exp(inp);
      Tensor uv = TdGroup.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  @Test
  void testSt1ExpLog() {
    Tensor inp = Tensors.vector(7, 3);
    Tensor xy = TdGroup.INSTANCE.exp(inp);
    Tensor uv = TdGroup.INSTANCE.log(xy);
    Tolerance.CHOP.requireClose(inp, uv);
  }

  @Test
  void testSt1LogExp() {
    Tensor inp = Tensors.vector(7, 3);
    Tensor uv = TdGroup.INSTANCE.log(inp);
    Tensor xy = TdGroup.INSTANCE.exp(uv);
    Tolerance.CHOP.requireClose(inp, xy);
  }

  @Test
  void testSt1Singular() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = Tensors.vector(0, Math.random());
      Tensor xy = TdGroup.INSTANCE.exp(inp);
      Tensor uv = TdGroup.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  @Test
  void testExpLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor v = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random());
      Scalar u = RealScalar.of(Math.random());
      Tensor inp = Append.of(v, u);
      Tensor xy = TdGroup.INSTANCE.exp(inp);
      Tensor uv = TdGroup.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  @Test
  void testLogExp() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random(), Math.random());
      Tensor uv = TdGroup.INSTANCE.log(inp);
      Tensor xy = TdGroup.INSTANCE.exp(uv);
      Tolerance.CHOP.requireClose(inp, xy);
    }
  }

  @Test
  void testSingular() {
    Tensor inp = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random(), 0);
    Tensor xy = TdGroup.INSTANCE.exp(inp);
    Tensor uv = TdGroup.INSTANCE.log(xy);
    Tolerance.CHOP.requireClose(inp, uv);
  }

  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(TdGroup.INSTANCE);
  private static final BarycentricCoordinate AFFINE = AffineWrap.of(TdGroup.INSTANCE);
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      // LeveragesCoordinate.slow(DtManifold.INSTANCE, InversePowerVariogram.of(1)), //
      // LeveragesCoordinate.slow(DtManifold.INSTANCE, InversePowerVariogram.of(2)), //
      AFFINE };

  @Test
  void testSimple3() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = n + 2; length < n + 8; ++length) {
          int fn = n;
          RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), fn, ExponentialDistribution.standard());
          Tensor sequence = RandomSample.of(rsi, random, length);
          Tensor mean1 = RandomSample.of(rsi, random);
          Tensor weights = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = TdBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._06.requireClose(mean1, mean2);
          // ---
          Tensor shift = RandomSample.of(rsi, random);
          for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
            Chop._03.requireClose(weights, //
                barycentricCoordinate.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
        }
  }

  @Test
  void testAffineBiinvariant() throws ClassNotFoundException, IOException {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = n + 2; length < n + 8; ++length) {
          barycentricCoordinate = Serialization.copy(barycentricCoordinate);
          RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), n, ExponentialDistribution.standard());
          Tensor sequence = RandomSample.of(rsi, random, length);
          Tensor mean1 = RandomSample.of(rsi, random);
          Tensor weights = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = TdBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._08.requireClose(mean1, mean2); // linear reproduction
          // ---
          Tensor shift = RandomSample.of(rsi, random);
          for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
            Chop._05.requireClose(weights, barycentricCoordinate.weights( //
                tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
        }
  }

  @Test
  void testAffineCenter() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = n + 2; length < n + 8; ++length) {
        RandomSampleInterface rsi = new TdRandomSample(UniformDistribution.of(-1, 1), n, ExponentialDistribution.standard());
        Tensor sequence = RandomSample.of(rsi, length);
        Tensor constant = AveragingWeights.of(length);
        Tensor center = TdBiinvariantMean.INSTANCE.mean(sequence, constant);
        Tensor weights = barycentricCoordinate.weights(sequence, center);
        Tolerance.CHOP.requireClose(weights, constant);
      }
  }
}
