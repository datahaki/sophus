// code by jph
package ch.alpine.sophus.hs.spd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.sca.Chop;

class SpdRiemannTest {
  @Test
  void testSimple2() {
    SpdRiemann spdRiemann = new SpdRiemann(IdentityMatrix.of(2));
    Tensor e11 = Tensors.fromString("{{1, 0}, {0, 0}}");
    Tensor e12 = Tensors.fromString("{{0, 1}, {1, 0}}");
    Scalar s1112 = spdRiemann.sectional(e11, e12);
    ExactScalarQ.require(s1112);
    assertEquals(s1112, RationalScalar.of(-1, 4));
  }

  @Test
  void testSimple3() {
    SpdRiemann spdRiemann = new SpdRiemann(IdentityMatrix.of(3));
    Tensor e11 = Tensors.fromString("{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}}");
    Tensor e12 = Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}");
    Tensor e13 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}");
    Scalar s1112 = spdRiemann.sectional(e11, e12);
    ExactScalarQ.require(s1112);
    assertEquals(s1112, RationalScalar.of(-1, 4));
    Scalar s1213 = spdRiemann.sectional(e12, e13);
    ExactScalarQ.require(s1213);
    assertEquals(s1213, RationalScalar.of(-1, 8));
  }

  @Test
  void testTransport3() {
    int n = 3;
    Tensor p = IdentityMatrix.of(n);
    RandomSampleInterface rsi = new Spd0RandomSample(n, TriangularDistribution.with(0, 1));
    Tensor q = RandomSample.of(rsi);
    SpdRiemann spdRiemann = new SpdRiemann(q);
    TensorUnaryOperator tuo = SpdTransport.INSTANCE.shift(p, q);
    Tensor e11 = tuo.apply(Tensors.fromString("{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}}"));
    Tensor e12 = tuo.apply(Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}"));
    Tensor e13 = tuo.apply(Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}"));
    Scalar s1112 = spdRiemann.sectional(e11, e12);
    Chop._10.requireClose(s1112, RationalScalar.of(-1, 4));
    Scalar s1213 = spdRiemann.sectional(e12, e13);
    Chop._10.requireClose(s1213, RationalScalar.of(-1, 8));
  }

  @Test
  void testMultiT() {
    Tensor e11 = Tensors.fromString("{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}}");
    Tensor e12 = Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}");
    Tensor e13 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}");
    Tensor p = IdentityMatrix.of(3);
    for (int c = 0; c < 5; ++c) {
      RandomSampleInterface rsi = new Spd0RandomSample(3, TriangularDistribution.with(0, 1));
      Tensor q = RandomSample.of(rsi);
      TensorUnaryOperator tuo = SpdTransport.INSTANCE.shift(p, q);
      e11 = tuo.apply(e11);
      e12 = tuo.apply(e12);
      e13 = tuo.apply(e13);
      SymmetricMatrixQ.require(e11);
      SymmetricMatrixQ.require(e12);
      SymmetricMatrixQ.require(e13);
      SpdRiemann spdRiemann = new SpdRiemann(q);
      Scalar s1112 = spdRiemann.sectional(e11, e12);
      Chop._10.requireClose(s1112, RationalScalar.of(-1, 4));
      Scalar s1213 = spdRiemann.sectional(e12, e13);
      Chop._10.requireClose(s1213, RationalScalar.of(-1, 8));
      p = q;
    }
  }
}
