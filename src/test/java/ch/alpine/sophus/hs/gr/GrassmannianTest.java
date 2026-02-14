// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class GrassmannianTest {
  @Test
  void testCurve() {
    Grassmannian grassmannian = new Grassmannian(2, 1);
    Tensor p = DiagonalMatrix.of(1, 0);
    Tensor q = DiagonalMatrix.of(0, 1);
    assertTrue(grassmannian.isPointQ().test(p));
    assertTrue(grassmannian.isPointQ().test(q));
  }

  @Test
  void testIllegal() {
    assertThrows(Exception.class, () -> new Grassmannian(0, +0));
    assertThrows(Exception.class, () -> new Grassmannian(4, -1));
    new Grassmannian(4, +0);
    assertThrows(Exception.class, () -> new Grassmannian(4, +5));
  }

  @Test
  void testGrassmannian() {
    int n = 2;
    Grassmannian grassmannian = new Grassmannian(n, 1);
    Tensor p = Tensors.fromString("{{1,0},{0,0}}");
    TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
    TensorUnaryOperator tuo = new TGrMemberQ(p)::defect;
    Tensor tensor = LinearSubspace.of(tuo, n, n).basis();
    assertEquals(tensor.length(), 1);
    GrExponential exponential = grassmannian.exponential(p);
    for (Tensor v : tensor) {
      tGrMemberQ.require(v);
      Tensor w = tGrMemberQ.projection(v);
      Tolerance.CHOP.requireClose(v, w);
    }
    Tensor v = tensor.get(0);
    Tensor q = exponential.exp(v.multiply(Pi.HALF));
    Tolerance.CHOP.requireClose(q, Tensors.fromString("{{0,0},{0,1}}"));
  }

  @Test
  void testGrassmannianRandom() {
    int n = 5;
    Grassmannian grassmannian = new Grassmannian(n, 3);
    Tensor p = RandomSample.of(grassmannian);
    TensorUnaryOperator tuo = m -> Join.of(p.dot(m).add(m.dot(p)).subtract(m), SymmetricMatrixQ.INSTANCE.defect(m));
    Tensor tensor = LinearSubspace.of(tuo, n, n).basis();
    assertEquals(tensor.length(), 6);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
    GrExponential exponential = grassmannian.exponential(p);
    for (Tensor v : tensor) {
      tGrMemberQ.require(v);
      Tensor w = tGrMemberQ.projection(v);
      Tolerance.CHOP.requireClose(v, w);
      Tensor q = exponential.exp(v.multiply(Pi.HALF));
      grassmannian.isPointQ().require(q);
    }
  }

  @Test
  void testSimple21() {
    Tensor p = Tensors.fromString("{{1,0},{0,0}}");
    TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
    assertTrue(tGrMemberQ.test(Tensors.fromString("{{0, 1}, {1, 0}}")));
    assertFalse(tGrMemberQ.test(Tensors.fromString("{{0, 1}, {2, 0}}")));
    assertFalse(tGrMemberQ.test(Tensors.fromString("{{0, 1}, {1, 1}}")));
    assertFalse(tGrMemberQ.test(Tensors.fromString("{{0, 1}, {-1, 0}}")));
  }

  @Test
  void testSimple31() {
    Tensor p = Tensors.fromString("{{1,0,0},{0,0,0},{0,0,0}}");
    TGrMemberQ tGr0MemberQ = new TGrMemberQ(p);
    assertTrue(tGr0MemberQ.test(Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}")));
    assertFalse(tGr0MemberQ.test(Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}}")));
  }

  @Test
  void testSimple32() {
    Tensor p = Tensors.fromString("{{1,0,0},{0,1,0},{0,0,0}}");
    TGrMemberQ tGr0MemberQ = new TGrMemberQ(p);
    assertFalse(tGr0MemberQ.test(Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}")));
    assertTrue(tGr0MemberQ.test(Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}}")));
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5 })
  void testRandom32(int n) {
    Distribution distribution = NormalDistribution.standard();
    for (int k = 1; k < n; ++k) {
      Grassmannian grassmannian = new Grassmannian(n, k);
      Tensor p = RandomSample.of(grassmannian);
      TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
      LinearSubspace linearSubspace = LinearSubspace.of(tGrMemberQ::defect, n, n);
      Tensor v = RandomVariate.of(distribution, n, n);
      Tensor v1 = tGrMemberQ.projection(v);
      tGrMemberQ.require(v1);
      assertFalse(tGrMemberQ.test(v));
      Tensor v2 = linearSubspace.projection(v);
      tGrMemberQ.require(v2);
      Tolerance.CHOP.requireClose(v1, v2);
      int dim = linearSubspace.basis().length();
      Tensor weights = RandomVariate.of(UniformDistribution.unit(20), dim);
      Tensor w = weights.dot(linearSubspace.basis()).multiply(RealScalar.of(.1));
      Tensor q = grassmannian.exponential(p).exp(w);
      grassmannian.isPointQ().test(q);
    }
  }

  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    for (int k = 1; k < 5; ++k) {
      RandomSampleInterface grRandomSample = Serialization.copy(new Grassmannian(k + 3, k));
      Tensor x = RandomSample.of(grRandomSample);
      GrManifold.INSTANCE.isPointQ().require(x);
    }
  }

  @Test
  void testNN() throws ClassNotFoundException, IOException {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface randomSampleInterface = Serialization.copy(new Grassmannian(n, n));
      Tensor x = RandomSample.of(randomSampleInterface);
      GrManifold.INSTANCE.isPointQ().require(x);
      Chop._09.requireClose(x, IdentityMatrix.of(n));
    }
  }

  @Test
  void testN0() {
    for (int n = 1; n < 5; ++n) {
      RandomSampleInterface randomSampleInterface = new Grassmannian(n, 0);
      for (int k = 0; k < 3; ++k) {
        Tensor x = RandomSample.of(randomSampleInterface);
        GrManifold.INSTANCE.isPointQ().require(x);
        assertEquals(x, Array.zeros(n, n));
        x.set(RealScalar.ONE::add, Tensor.ALL, Tensor.ALL);
      }
    }
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> new Grassmannian(-3, 2));
    assertThrows(Exception.class, () -> new Grassmannian(3, 4));
  }
}
