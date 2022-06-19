// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.dv.LeveragesGenesis;
import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.hs.ad.HsBarycentricCoordinate;
import ch.alpine.sophus.hs.ad.HsBiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.spa.SparseArray;

class GrAlgebraTest {
  @Test
  void testSimple() {
    Random random = new Random(2);
    HsAlgebra hsAlgebra = GrAlgebra.of(5, 2, 6);
    assertInstanceOf(SparseArray.class, hsAlgebra.ad());
    int n = 6;
    assertEquals(hsAlgebra.dimM(), n);
    assertEquals(hsAlgebra.dimH(), 1 + 3);
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    HsBarycentricCoordinate hsBarycentricCoordinate = new HsBarycentricCoordinate(hsAlgebra, LeveragesGenesis.DEFAULT);
    Tensor sequence = RandomVariate.of(distribution, random, n + 2, n);
    Tensor x = RandomVariate.of(distribution, n);
    Tensor weights = hsBarycentricCoordinate.weights(sequence, x);
    BiinvariantMean biinvariantMean = HsBiinvariantMean.of(hsAlgebra);
    Tensor mean = biinvariantMean.mean(sequence, weights);
    Tolerance.CHOP.requireClose(x, mean);
  }

  @Test
  void testLarge() {
    HsAlgebra hsAlgebra = GrAlgebra.of(7, 3, 6);
    assertEquals(hsAlgebra.dimG(), 7 * 6 / 2);
  }

  @Test
  void testBasis() {
    assertInstanceOf(SparseArray.class, GrAlgebra.basis(5, 2));
  }
}
