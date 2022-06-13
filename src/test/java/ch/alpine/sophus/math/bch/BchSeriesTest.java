// code by jph
package ch.alpine.sophus.math.bch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.sl.SlAlgebra;
import ch.alpine.sophus.lie.so.SoAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.sca.Chop;

class BchSeriesTest {
  private static final Distribution DISTRIBUTION = DiscreteUniformDistribution.of(-10, 11);
  private static final LieAlgebra[] LIE_ALGEBRAS = new LieAlgebra[] { //
      SlAlgebra.of(3), //
      SoAlgebra.of(4), //
      SoAlgebra.of(5) };

  @Test
  void test6() throws ClassNotFoundException, IOException {
    for (LieAlgebra lieAlgebra : LIE_ALGEBRAS) {
      Tensor ad = lieAlgebra.ad();
      BchSeries6 bchSeries6 = Serialization.copy(new BchSeries6(ad));
      BakerCampbellHausdorff bakerCampbellHausdorff = new BakerCampbellHausdorff(ad, 6, Chop.NONE);
      int n = ad.length();
      Tensor x = RandomVariate.of(DISTRIBUTION, n);
      Tensor y = RandomVariate.of(DISTRIBUTION, n);
      assertEquals(bchSeries6.series(x, y), bakerCampbellHausdorff.series(x, y));
    }
  }

  @Test
  void test8() {
    for (LieAlgebra lieAlgebra : LIE_ALGEBRAS) {
      Tensor ad = lieAlgebra.ad();
      BchSeries8 bchSeries8 = new BchSeries8(ad);
      BakerCampbellHausdorff bakerCampbellHausdorff = new BakerCampbellHausdorff(ad, 8, Chop.NONE);
      int n = ad.length();
      Tensor x = RandomVariate.of(DISTRIBUTION, n);
      Tensor y = RandomVariate.of(DISTRIBUTION, n);
      assertEquals(bchSeries8.series(x, y), bakerCampbellHausdorff.series(x, y));
    }
  }

  @Test
  void testQuad() {
    for (LieAlgebra lieAlgebra : LIE_ALGEBRAS) {
      Tensor ad = lieAlgebra.ad();
      int n = ad.length();
      Tensor x = RandomVariate.of(DISTRIBUTION, n);
      Tensor y = RandomVariate.of(DISTRIBUTION, n);
      Tensor adx = ad.dot(x);
      Tensor ady = ad.dot(y);
      Tensor r1 = Dot.of(adx, ady, adx, y);
      Tensor r2 = Dot.of(ady, adx, adx, y);
      assertEquals(r1, r2);
    }
  }
}
