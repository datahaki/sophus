// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.mat.re.MatrixRank;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.LogisticDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.d.PoissonDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

class TGrMemberQTest {
  @Test
  void testSerializable() throws ClassNotFoundException, IOException {
    int n = 5;
    Tensor x = RandomSample.of(new Grassmannian(n, 3));
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    GrManifold.INSTANCE.isPointQ().require(x);
    TGrMemberQ tGrMemberQ = Serialization.copy(new TGrMemberQ(x));
    Tensor pre = RandomVariate.of(NormalDistribution.standard(), n, n);
    assertFalse(tGrMemberQ.test(pre));
    Tensor w1 = tGrMemberQ.projection(pre);
    tGrMemberQ.require(w1);
    assertFalse(Chop._08.allZero(w1));
    Tensor w2 = tGrMemberQ.projection(w1);
    tGrMemberQ.require(w2);
    assertFalse(Chop._08.allZero(w2));
    Tolerance.CHOP.requireClose(w1, w2);
  }

  @ParameterizedTest
  @ValueSource(ints = { 3, 4, 5 })
  void testProjection(int n) {
    int k = 2;
    RandomSampleInterface randomSampleInterface = new Grassmannian(n, k);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Tensor v = new GrExponential(p).log(q);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(p); // .require(v);
    LinearSubspace linearSubspace = LinearSubspace.of(tGrMemberQ::defect, n, n);
    assertFalse(linearSubspace.basis().stream().anyMatch(ExactTensorQ::of));
    assertEquals(linearSubspace.dimensions(), k * (n - k));
    tGrMemberQ.require(v);
    assertFalse(Chop._08.allZero(v));
    Sign.requirePositive(FrobeniusNorm.of(v));
    Tolerance.CHOP.requireAllZero(TGrMemberQ.identity(p, v));
    Tensor w1 = tGrMemberQ.projection(v);
    tGrMemberQ.require(w1);
    assertFalse(Chop._08.allZero(w1));
    Tensor w2 = tGrMemberQ.projection(v);
    Tensor w3 = linearSubspace.projection(v);
    Tolerance.CHOP.requireClose(w2, w3);
    tGrMemberQ.require(w2);
    assertFalse(Chop._08.allZero(w2));
    Tolerance.CHOP.requireClose(w1, w2);
    Sign.requirePositive(FrobeniusNorm.of(w1));
    Tolerance.CHOP.requireAllZero(TGrMemberQ.identity(p, w1));
  }

  @Test
  void testDimensionsX() {
    Random randomGenerator = new Random(3);
    Distribution distribution = LogisticDistribution.of(0, 3);
    for (int n = 1; n < 6; ++n) {
      int fn = n;
      for (int k = 0; k <= n; ++k) {
        Tensor x = RandomSample.of(new Grassmannian(n, k), randomGenerator);
        TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
        int expected = k * (n - k);
        Tensor samples = Tensor.of(IntStream.range(0, expected + 3) //
            .mapToObj(_ -> Flatten.of(tGrMemberQ.projection(RandomVariate.of(distribution, randomGenerator, fn, fn)))));
        int r = MatrixRank.of(samples);
        Integers.requireEquals(r, expected);
      }
    }
  }

  @Test
  void testDimensionsExact() {
    Distribution distribution = PoissonDistribution.of(10);
    for (int n = 1; n < 6; ++n) {
      int fn = n;
      for (int k = 0; k <= n; ++k) {
        Tensor diagonal = Join.of(ConstantArray.of(RealScalar.ONE, k), ConstantArray.of(RealScalar.ZERO, n - k));
        Tensor x = DiagonalMatrix.sparse(diagonal);
        TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
        GrManifold.INSTANCE.isPointQ().require(x);
        int expected = k * (n - k);
        Tensor samples = Tensor.of(IntStream.range(0, expected + 5) //
            .mapToObj(_ -> Flatten.of(tGrMemberQ.projection(RandomVariate.of(distribution, fn, fn)))));
        int r = MatrixRank.of(samples);
        assertEquals(r, expected);
        int dim = k * (n - k);
        if (0 < dim) {
          LinearSubspace linearSubspace = LinearSubspace.of(tGrMemberQ::defect, n, n);
          assertEquals(linearSubspace.dimensions(), dim);
          // IO.println("---");
          // linearSubspace.basis().forEach(b -> IO.println(Pretty.of(b)));
        }
      }
    }
  }
}
