// code by jph
package ch.alpine.sophus.lie.se;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.gbc.LeveragesGenesis;
import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.hs.ad.HsBarycentricCoordinate;
import ch.alpine.sophus.hs.ad.HsBiinvariantMean;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import junit.framework.TestCase;

public class SeAlgebraTest extends TestCase {
  public void testOne() {
    LieAlgebra lieAlgebra = SeAlgebra.of(1);
    assertEquals(lieAlgebra.ad(), Array.zeros(1, 1, 1));
  }

  public void testSimple() {
    LieAlgebra lieAlgebra = SeAlgebra.of(4);
    assertEquals(lieAlgebra.ad().length(), 4 + 6);
  }

  public void testHs() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    for (int n = 2; n < 5; ++n) {
      LieAlgebra lieAlgebra = SeAlgebra.of(n);
      Tensor ad = lieAlgebra.ad();
      int fn = n;
      AssertFail.of(() -> new HsAlgebra(ad, fn - 1, 8));
      if (2 < n)
        AssertFail.of(() -> new HsAlgebra(ad, fn + 1, 8));
      HsAlgebra hsAlgebra = new HsAlgebra(ad, n, 10);
      Tensor g = RandomVariate.of(distribution, ad.length());
      Tensor m = RandomVariate.of(distribution, n);
      hsAlgebra.action(g, m);
      HsBarycentricCoordinate hsBarycentricCoordinate = new HsBarycentricCoordinate(hsAlgebra, LeveragesGenesis.DEFAULT);
      Tensor sequence = RandomVariate.of(distribution, n + 2, n);
      Tensor x = RandomVariate.of(distribution, n);
      Tensor weights = hsBarycentricCoordinate.weights(sequence, x);
      BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
      Tensor mean = biinvariantMean.mean(sequence, weights);
      Tolerance.CHOP.requireClose(x, mean);
    }
  }

  public void testNFail() {
    AssertFail.of(() -> SeAlgebra.of(0));
    AssertFail.of(() -> SeAlgebra.of(-1));
  }
}
