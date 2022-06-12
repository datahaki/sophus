// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.MatrixRank;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.LogisticDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.d.PoissonDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

class TGrMemberQTest {
  @Test
  public void testSerializable() throws ClassNotFoundException, IOException {
    int n = 5;
    Tensor x = RandomSample.of(new GrRandomSample(n, 3));
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    GrMemberQ.INSTANCE.require(x);
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

  @Test
  public void testProjection() {
    for (int n = 3; n < 6; ++n) {
      RandomSampleInterface randomSampleInterface = new GrRandomSample(n, 2); // 4 dimensional
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor v = new GrExponential(p).log(q);
      TGrMemberQ tGrMemberQ = new TGrMemberQ(p); // .require(v);
      tGrMemberQ.require(v);
      assertFalse(Chop._08.allZero(v));
      Sign.requirePositive(FrobeniusNorm.of(v));
      Tolerance.CHOP.requireAllZero(GrIdentities.of(p, v));
      Tensor w1 = tGrMemberQ.projection(v);
      tGrMemberQ.require(w1);
      assertFalse(Chop._08.allZero(w1));
      Tensor w2 = tGrMemberQ.projection(v);
      tGrMemberQ.require(w2);
      assertFalse(Chop._08.allZero(w2));
      Tolerance.CHOP.requireClose(w1, w2);
      Sign.requirePositive(FrobeniusNorm.of(w1));
      Tolerance.CHOP.requireAllZero(GrIdentities.of(p, w1));
    }
  }

  @Test
  public void testDimensionsX() {
    Random random = new Random(3);
    Distribution distribution = LogisticDistribution.of(0, 3);
    for (int n = 1; n < 6; ++n) {
      int fn = n;
      for (int k = 0; k <= n; ++k) {
        Tensor x = RandomSample.of(new GrRandomSample(n, k), random);
        TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
        int expected = k * (n - k);
        Tensor samples = Tensor.of(IntStream.range(0, expected + 3) //
            .mapToObj(i -> Flatten.of(tGrMemberQ.projection(RandomVariate.of(distribution, random, fn, fn)))));
        int r = MatrixRank.of(samples);
        Integers.requireEquals(r, expected);
      }
    }
  }

  @Test
  public void testDimensionsExact() {
    Distribution distribution = PoissonDistribution.of(10);
    for (int n = 1; n < 6; ++n) {
      int fn = n;
      for (int k = 0; k <= n; ++k) {
        Tensor diagon = Join.of(ConstantArray.of(RealScalar.ONE, k), ConstantArray.of(RealScalar.ZERO, n - k));
        Tensor x = DiagonalMatrix.with(diagon);
        GrMemberQ.INSTANCE.require(x);
        TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
        int expected = k * (n - k);
        Tensor samples = Tensor.of(IntStream.range(0, expected + 5) //
            .mapToObj(i -> Flatten.of(tGrMemberQ.projection(RandomVariate.of(distribution, fn, fn)))));
        int r = MatrixRank.of(samples);
        assertEquals(r, expected);
      }
    }
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> new TGrMemberQ(null));
  }
}
