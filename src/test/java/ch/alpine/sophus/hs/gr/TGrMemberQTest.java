// code by jph
package ch.alpine.sophus.hs.gr;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.MatrixRank;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.LogisticDistribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.PoissonDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Sign;
import junit.framework.TestCase;

public class TGrMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    int n = 5;
    Tensor x = RandomSample.of(GrRandomSample.of(n, 3));
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    GrMemberQ.INSTANCE.require(x);
    TGrMemberQ tGrMemberQ = Serialization.copy(new TGrMemberQ(x));
    Tensor pre = RandomVariate.of(NormalDistribution.standard(), n, n);
    assertFalse(tGrMemberQ.test(pre));
    final Tensor v = tGrMemberQ.forceProject(pre).multiply(Pi.VALUE);
    tGrMemberQ.require(v);
    // System.out.println(Pretty.of(v.map(Round._3)));
    // System.out.println(SymmetricMatrixQ.of(v));
    // System.out.println("SOME:" + Pretty.of(x.dot(v).add(v.dot(x)).subtract(v).map(Round._3)));
    // Tensor w = ;
    // System.out.println("PROJECTED"+Pretty.of(w.map(Round._3)));
    // Tolerance.CHOP.requireClose(v, tGrMemberQ.project(v));
    // Tensor s = tGrMemberQ.project(pre);
    // tGrMemberQ.require(s);
  }

  public void testProject() {
    int n = 4;
    Distribution distribution = UniformDistribution.unit();
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, 2); // 4 dimensional
    for (int count = 0; count < 5; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor v = new GrExponential(p).log(q);
      TGrMemberQ tGrMemberQ = new TGrMemberQ(p); // .require(v);
      tGrMemberQ.require(v);
      Tolerance.CHOP.requireAllZero(GrIdentities.of(p, v));
      Tensor w = tGrMemberQ.forceProject(RandomVariate.of(distribution, n, n));
      tGrMemberQ.require(w);
      Sign.requirePositive(FrobeniusNorm.of(w));
      Tolerance.CHOP.requireAllZero(GrIdentities.of(p, w));
      // System.out.println(Pretty.of(v.map(Round._3)));
      // Tensor w = tGrMemberQ.project(v);
      // System.out.println(Pretty.of(w.map(Round._3)));
      // Chop._10.requireClose(v, w);
    }
  }

  public void testDimensionsX() {
    Random random = new Random(3);
    Distribution distribution = LogisticDistribution.of(0, 3);
    for (int n = 1; n < 6; ++n) {
      int fn = n;
      for (int k = 0; k <= n; ++k) {
        Tensor x = RandomSample.of(GrRandomSample.of(n, k), random);
        TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
        int expected = k * (n - k);
        Tensor samples = Tensor.of(IntStream.range(0, expected + 3) //
            .mapToObj(i -> Flatten.of(tGrMemberQ.forceProject(RandomVariate.of(distribution, random, fn, fn)))));
        int r = MatrixRank.of(samples);
        Integers.requireEquals(r, expected);
      }
    }
  }

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
            .mapToObj(i -> Flatten.of(tGrMemberQ.forceProject(RandomVariate.of(distribution, fn, fn)))));
        int r = MatrixRank.of(samples);
        assertEquals(r, expected);
      }
    }
  }

  public void testNullFail() {
    AssertFail.of(() -> new TGrMemberQ(null));
  }
}
