// code by jph
package ch.alpine.sophus.lie.se;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.dv.LeveragesGenesis;
import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.hs.ad.HsBarycentricCoordinate;
import ch.alpine.sophus.hs.ad.HsBiinvariantMean;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class SeAlgebraTest {
  @Test
  void testOne() {
    LieAlgebra lieAlgebra = SeAlgebra.of(1);
    assertEquals(lieAlgebra.ad(), Array.zeros(1, 1, 1));
  }

  @Test
  void testSimple() {
    LieAlgebra lieAlgebra = SeAlgebra.of(4);
    assertEquals(lieAlgebra.ad().length(), 4 + 6);
  }

  @Test
  void testHs() {
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    for (int n = 2; n < 5; ++n) {
      LieAlgebra lieAlgebra = SeAlgebra.of(n);
      Tensor ad = lieAlgebra.ad();
      int fn = n;
      assertThrows(Exception.class, () -> new HsAlgebra(ad, fn - 1, 8));
      if (2 < n)
        assertThrows(Exception.class, () -> new HsAlgebra(ad, fn + 1, 8));
      HsAlgebra hsAlgebra = new HsAlgebra(ad, n, 10);
      Tensor g = RandomVariate.of(distribution, random, ad.length());
      Tensor m = RandomVariate.of(distribution, random, n);
      hsAlgebra.action(g, m);
      HsBarycentricCoordinate hsBarycentricCoordinate = new HsBarycentricCoordinate(hsAlgebra, LeveragesGenesis.DEFAULT);
      Tensor sequence = RandomVariate.of(distribution, random, n + 2, n);
      Tensor x = RandomVariate.of(distribution, random, n);
      Tensor weights = hsBarycentricCoordinate.weights(sequence, x);
      BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
      Tensor mean = biinvariantMean.mean(sequence, weights);
      Tolerance.CHOP.requireClose(x, mean);
    }
  }

  @Test
  void testNFail() {
    assertThrows(Exception.class, () -> SeAlgebra.of(0));
    assertThrows(Exception.class, () -> SeAlgebra.of(-1));
  }
}
