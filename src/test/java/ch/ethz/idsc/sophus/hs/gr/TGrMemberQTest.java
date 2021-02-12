// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.MatrixRank;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.LogisticDistribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.PoissonDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class TGrMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    int n = 8;
    Tensor x = RandomSample.of(GrRandomSample.of(n, 3));
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    GrMemberQ.INSTANCE.require(x);
    Tensor pre = RandomVariate.of(NormalDistribution.standard(), n, n);
    TGrMemberQ tGrMemberQ = Serialization.copy(new TGrMemberQ(x));
    assertFalse(tGrMemberQ.test(pre));
    Tensor v = tGrMemberQ.project(pre).multiply(Pi.VALUE);
    tGrMemberQ.require(v);
  }

  public void testDimensionsX() {
    Distribution distribution = LogisticDistribution.of(0, 3);
    for (int n = 1; n < 6; ++n) {
      int fn = n;
      for (int k = 0; k <= n; ++k) {
        Tensor x = RandomSample.of(GrRandomSample.of(n, k));
        TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
        int expected = k * (n - k);
        Tensor samples = Tensor.of(IntStream.range(0, expected + 3) //
            .mapToObj(i -> Flatten.of(tGrMemberQ.project(RandomVariate.of(distribution, fn, fn)))));
        int r = MatrixRank.of(samples);
        assertEquals(r, expected);
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
            .mapToObj(i -> Flatten.of(tGrMemberQ.project(RandomVariate.of(distribution, fn, fn)))));
        int r = MatrixRank.of(samples);
        assertEquals(r, expected);
      }
    }
  }

  public void testNullFail() {
    AssertFail.of(() -> new TGrMemberQ(null));
  }
}
